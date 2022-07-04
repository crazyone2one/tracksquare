package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.UserGroupConstants;
import io.square.common.constants.UserGroupType;
import io.square.dto.RelatedSource;
import io.square.dto.WorkspaceResource;
import io.square.entity.Group;
import io.square.entity.Project;
import io.square.entity.UserGroup;
import io.square.entity.Workspace;
import io.square.exception.BizException;
import io.square.mapper.*;
import io.square.service.WorkspaceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkspaceServiceImpl extends ServiceImpl<WorkspaceMapper, Workspace> implements WorkspaceService {

    @Resource
    UserGroupMapper userGroupMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    GroupMapper groupMapper;
    @Resource
    WorkspaceMapper workspaceMapper;
    @Resource
    ProjectMapper projectMapper;

    @Override
    public ResponseResult<Workspace> saveWorkspace(Workspace workspace) {
        if (StringUtils.isBlank(workspace.getName())) {
            BizException.throwException("工作空间名不能为空");
        }
        checkWorkspace(workspace);
        if (StringUtils.isNotBlank(workspace.getId())) {
            baseMapper.updateById(workspace);
        }else {
            baseMapper.insert(workspace);
            // 创建工作空间为当前用户添加用户组
            UserGroup build = UserGroup.builder().userId(workspace.getCreateUser()).groupId(UserGroupConstants.WS_ADMIN)
                    .sourceId(workspace.getId()).build();
            userGroupMapper.insert(build);
            // 新项目创建新工作空间时设置
            userMapper.updateLastWorkspaceIdIfNull(workspace.getId(), workspace.getCreateUser());
        }
        return ResponseResult.success(workspace);
    }

    @Override
    public ResponseResult<Workspace> addWorkspaceByAdmin(Workspace workspace) {
        checkWorkspace(workspace);
        baseMapper.insert(workspace);
        // 创建工作空间为当前用户添加用户组
        UserGroup build = UserGroup.builder().userId(workspace.getCreateUser()).groupId(UserGroupConstants.WS_ADMIN)
                .sourceId(workspace.getId()).build();
        userGroupMapper.insert(build);
        return ResponseResult.success(workspace);
    }

    @Override
    public ResponseResult<Map<String, Object>> getAllWorkspaceList(Workspace workspace, long page, long limit) {
        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(workspace.getName()), Workspace::getName, workspace.getName());
        wrapper.orderByDesc(Workspace::getUpdateTime);
        Page<Workspace> selectPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", selectPage.getTotal());
        result.put("records", selectPage.getRecords());
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<List<Workspace>> getWorkspaceList() {
        List<Workspace> workspaces = baseMapper.selectList(new QueryWrapper<Workspace>().lambda().orderByDesc(Workspace::getUpdateTime));
        return ResponseResult.success(workspaces);
    }

    @Override
    public ResponseResult<WorkspaceResource> listResource(String groupId, String type) {
        Group group = groupMapper.selectById(groupId);
        String workspaceId = group.getScopeId();
        WorkspaceResource resource = new WorkspaceResource();
        if (StringUtils.equals(UserGroupType.WORKSPACE, type)) {
            LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(!StringUtils.equals(workspaceId, "global"), Workspace::getId, workspaceId);
            List<Workspace> workspaces = workspaceMapper.selectList(wrapper);
            resource.setWorkspaces(workspaces);
        }

        if (StringUtils.equals(UserGroupType.PROJECT, type)) {
            LambdaQueryWrapper<Project> pm = new LambdaQueryWrapper<>();

            if (!StringUtils.equals(workspaceId, "global")) {
                LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq( Workspace::getId, workspaceId);
                List<Workspace> workspaces = workspaceMapper.selectList(wrapper);
                List<String> list = workspaces.stream().map(Workspace::getId).collect(Collectors.toList());
                pm.in(Project::getWorkspaceId, list);
            }
            List<Project> projects = projectMapper.selectList(pm);
            resource.setProjects(projects);
        }

        return ResponseResult.success(resource);
    }

    @Override
    public ResponseResult<List<Workspace>> getWorkspaceListByUserId(String userId) {
        List<RelatedSource> relatedSource = userGroupMapper.getRelatedSource(userId);
        List<String> wsIds = relatedSource
                .stream()
                .map(RelatedSource::getWorkspaceId)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(wsIds)){
            return ResponseResult.success(new ArrayList<>());
        }
        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CollectionUtils.isNotEmpty(wsIds), Workspace::getId, wsIds);
        return ResponseResult.success(baseMapper.selectList(wrapper));
    }

    private void checkWorkspace(Workspace workspace) {
        if (StringUtils.isBlank(workspace.getName())) {
            BizException.throwException("工作空间名不能为空");
        }
        LambdaQueryWrapper<Workspace> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Workspace::getName, workspace.getName());
        wrapper.ne(StringUtils.isNotBlank(workspace.getId()), Workspace::getId, workspace.getId());
        if (baseMapper.selectCount(wrapper) > 0) {
            BizException.throwException("工作空间名已存在");
        }
    }
}
