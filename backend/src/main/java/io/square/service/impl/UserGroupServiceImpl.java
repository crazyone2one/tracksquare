package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.UserGroupType;
import io.square.entity.*;
import io.square.mapper.GroupMapper;
import io.square.mapper.ProjectMapper;
import io.square.mapper.UserGroupMapper;
import io.square.mapper.WorkspaceMapper;
import io.square.service.UserGroupService;
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
 * @since 2022-06-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements UserGroupService {

    @Resource
    GroupMapper groupMapper;
    @Resource
    WorkspaceMapper workspaceMapper;
    @Resource
    ProjectMapper projectMapper;

    @Override
    public ResponseResult<List<UserGroup>> getWorkspaceMemberGroups(String workspaceId, String userId) {
        return null;
    }

    @Override
    public ResponseResult<Map<String, Object>> getGroupUser(Group group, long page, long limit) {
        Page<User> userPage = new Page<>(page, limit);
        IPage<User> iPage = baseMapper.getGroupUser(userPage, group);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<List<?>> getGroupSource(String groupId, String userId) {
        LambdaQueryWrapper<UserGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroup::getUserId, userId).eq(UserGroup::getGroupId, groupId);
        List<UserGroup> userGroups = baseMapper.selectList(wrapper);
        List<String> sources = userGroups.stream().map(UserGroup::getSourceId).collect(Collectors.toList());
        if (sources.isEmpty()) {
            return ResponseResult.success(new ArrayList<>());
        }
        Group group = groupMapper.selectById(groupId);
        String type = group.getType();
        if (StringUtils.equals(type, UserGroupType.WORKSPACE)) {
            LambdaQueryWrapper<Workspace> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.in(Workspace::getId, sources);
            return ResponseResult.success(workspaceMapper.selectList(wrapper1));
        }
        if (StringUtils.equals(type, UserGroupType.PROJECT)){
            LambdaQueryWrapper<Project> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.in(Project::getId, sources);
            return ResponseResult.success(projectMapper.selectList(wrapper2));
        }
        return null;
    }
}
