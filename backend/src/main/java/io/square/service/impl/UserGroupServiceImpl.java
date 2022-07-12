package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import io.square.common.ResponseResult;
import io.square.common.constants.UserGroupType;
import io.square.dto.*;
import io.square.entity.*;
import io.square.mapper.*;
import io.square.service.UserGroupService;
import io.square.service.WorkspaceService;
import io.square.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
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
    @Resource
    WorkspaceService workspaceService;
    @Resource
    UserGroupPermissionMapper userGroupPermissionMapper;
    private static final String GLOBAL = "global";

    @Override
    public ResponseResult<List<Group>> getWorkspaceMemberGroups(String workspaceId, String userId) {
        return ResponseResult.success(groupMapper.getWorkspaceMemberGroups(workspaceId, userId));
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
        if (StringUtils.equals(type, UserGroupType.PROJECT)) {
            LambdaQueryWrapper<Project> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.in(Project::getId, sources);
            return ResponseResult.success(projectMapper.selectList(wrapper2));
        }
        return null;
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> getAllUserGroup(String userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        LambdaQueryWrapper<UserGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroup::getUserId, userId);
        List<UserGroup> userGroups = baseMapper.selectList(wrapper);
        List<String> groupsIds = userGroups.stream().map(UserGroup::getGroupId).distinct().collect(Collectors.toList());
        groupsIds.forEach(id -> {
            Group group = groupMapper.selectById(id);
            String type = group.getType();
            Map<String, Object> map = new HashMap<>(2);
            map.put("type", id + "+" + type);
            WorkspaceResource workspaceResource = workspaceService.listResource(id, group.getType()).getData();
            List<String> collect = userGroups.stream().filter(ugp -> ugp.getGroupId().equals(id)).map(UserGroup::getSourceId).collect(Collectors.toList());
            map.put("ids", collect);
            if (StringUtils.equals(type, UserGroupType.WORKSPACE)) {
                map.put("workspaces", workspaceResource.getWorkspaces());
            }
            if (StringUtils.equals(type, UserGroupType.PROJECT)) {
                map.put("projects", workspaceResource.getProjects());
            }
            list.add(map);
        });
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult<List<Group>> getGroupsByType(Group request) {
        String resourceId = request.getResourceId();
        String type = request.getType();
        List<String> scopeList = Arrays.asList(GLOBAL, resourceId);
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Group::getScopeId, scopeList).eq(Group::getType, type);
        return ResponseResult.success(groupMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<List<Group>> getGroupByType(Group request) {
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        String type = request.getType();
        if (StringUtils.isBlank(type)) {
            return ResponseResult.success(new ArrayList<>());
        }
        wrapper.eq(!StringUtils.equals(type, UserGroupType.SYSTEM), Group::getType, type);
        return ResponseResult.success(groupMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<GroupPermissionDTO> getGroupResource(Group g) {
        GroupPermissionDTO dto = new GroupPermissionDTO();
        InputStream permission = getClass().getResourceAsStream("/permission.json");
        String type = g.getType();
        String id = g.getId();
        LambdaQueryWrapper<UserGroupPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroupPermission::getGroupId, id);
        List<UserGroupPermission> userGroupPermissions = userGroupPermissionMapper.selectList(wrapper);
        List<String> permissionList = userGroupPermissions.stream().map(UserGroupPermission::getPermissionId).collect(Collectors.toList());
        if (Objects.isNull(permission)) {
            throw new RuntimeException("读取文件失败!");
        } else {
            GroupJson group;
            try {
                group = JacksonUtils.convertValue(permission, new TypeReference<>() {
                });
                List<GroupResource> resource = group.getResource();
                List<GroupPermission> permissions = group.getPermissions();
                List<GroupResourceDTO> dtoPermissions = dto.getPermissions();
                dtoPermissions.addAll(getResourcePermission(resource, permissions, type, permissionList));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return ResponseResult.success(dto);
    }

    @Override
    public ResponseResult<List<Group>> getProjectMemberGroups(String projectId, String userId) {
        return ResponseResult.success(baseMapper.getProjectMemberGroups(projectId, userId));
    }

    private List<GroupResourceDTO> getResourcePermission(List<GroupResource> resource, List<GroupPermission> permissions, String type, List<String> permissionList) {
        List<GroupResourceDTO> dto = new ArrayList<>();
        List<GroupResource> resources = resource.stream().filter(g -> g.getId().startsWith(type) || g.getId().startsWith("PERSONAL")).collect(Collectors.toList());
        permissions.forEach(p -> {
            if (permissionList.contains(p.getId())) {
                p.setChecked(true);
            }
        });
        for (GroupResource r : resources) {
            GroupResourceDTO resourceDTO = new GroupResourceDTO();
            resourceDTO.setResource(r);
            List<GroupPermission> collect = permissions
                    .stream()
                    .filter(p -> StringUtils.equals(r.getId(), p.getResourceId()))
                    .collect(Collectors.toList());
            resourceDTO.setPermissions(collect);
            resourceDTO.setType(r.getId().split("_")[0]);
            dto.add(resourceDTO);
        }
        return dto;
    }

}
