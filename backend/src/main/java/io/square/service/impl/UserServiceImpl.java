package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.common.constants.UserGroupType;
import io.square.controller.request.AddMemberRequest;
import io.square.controller.request.QueryMemberRequest;
import io.square.entity.Group;
import io.square.entity.Project;
import io.square.entity.User;
import io.square.entity.UserGroup;
import io.square.mapper.GroupMapper;
import io.square.mapper.ProjectMapper;
import io.square.mapper.UserGroupMapper;
import io.square.mapper.UserMapper;
import io.square.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-13
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    ProjectMapper projectMapper;
    @Resource
    UserGroupMapper userGroupMapper;
    @Resource
    GroupMapper groupMapper;

    @Override
    public List<User> getUsersList() {
        return baseMapper.selectList(null);
    }

    @Override
    public ResponseResult<Map<String, Object>> getPageList(User user, long page, long limit) {
        Page<User> producePage = new Page<>(page, limit);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(user.getName()), User::getName, user.getName());
        Page<User> userPage = baseMapper.selectPage(producePage, wrapper);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", userPage.getTotal());
        result.put("records", userPage.getRecords());
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<User> updateCurrentUser(User user) {
        User selectById = baseMapper.selectById(user.getId());
        selectById.setLastProjectId(user.getLastProjectId());
        selectById.setUpdateTime(LocalDate.now());
        baseMapper.updateById(selectById);
        return ResponseResult.success(selectById);
    }

    @Override
    public ResponseResult<List<User>> getProjectMember(QueryMemberRequest request) {
        return ResponseResult.success(baseMapper.selectList(null));
    }

    @Override
    public Map<String, User> queryNameByIds(List<String> userIds) {
        if (userIds.isEmpty()) {
            return new HashMap<>(0);
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getId, userIds);
        return baseMapper.queryNameByIds(wrapper);
    }

    @Override
    public ResponseResult<List<User>> getMemberList(User user) {
        return ResponseResult.success(baseMapper.getMemberList(user));
    }

    @Override
    public ResponseResult<List<User>> getUserList() {
        List<User> users = baseMapper.selectList(new LambdaQueryWrapper<User>().orderByDesc(User::getUpdateTime));
        return ResponseResult.success(users);
    }

    @Override
    public ResponseResult<User> switchUserResource(String sourceId, String userId, String sign) {
        User user = baseMapper.selectById(userId);
        if (StringUtils.equals("workspace", sign)) {
            user.setLastWorkspaceId(sourceId);
            List<Project> projects = getProjectListByWsAndUserId(sourceId, userId);
            if (projects.size() > 0) {
                user.setLastProjectId(projects.get(0).getId());
            } else {
                user.setLastProjectId("");
            }
        }
        baseMapper.updateById(user);
        return ResponseResult.success(user);
    }

    @Override
    public ResponseResult<Map<String, Object>> getMemberList(QueryMemberRequest request, long page, long limit) {
        IPage<User> iPage = baseMapper.getMemberList(new Page<>(page, limit), request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseResult.success(result);
    }

    @Override
    public ResponseResult<String> addWorkspaceMember(AddMemberRequest request) {
        return null;
    }

    @Override
    public void addGroupMember(String type, String sourceId, List<String> userIds, List<String> groupIds) {
        if (!StringUtils.equalsAny(type, "PROJECT", "WORKSPACE") || StringUtils.isBlank(sourceId) || CollectionUtils.isEmpty(userIds) || CollectionUtils.isEmpty(groupIds)) {
            return;
        }
        List<String> dbOptionalGroupIds = getGroupIdsByType(type, sourceId);
        for (String userId : userIds) {
            User user = baseMapper.selectById(userId);
            if (Objects.isNull(user)) {
                log.info("add member warning, invalid user id: " + userId);
                continue;
            }
            List<String> toAddGroupIds = new ArrayList<>(groupIds);
            List<String> existGroupIds = getUserExistSourceGroup(userId, sourceId);
            toAddGroupIds.removeAll(existGroupIds);
            toAddGroupIds.retainAll(dbOptionalGroupIds);
            for (String groupId : toAddGroupIds) {
                UserGroup build = UserGroup.builder().userId(userId).groupId(groupId).sourceId(sourceId).build();
                userGroupMapper.insert(build);
            }
        }
    }

    @Override
    public ResponseResult<String> deleteMember(String userId, String workspaceId) {
        LambdaQueryWrapper<Group> groupQueryWrapper = new LambdaQueryWrapper<>();
        groupQueryWrapper.eq(Group::getType, UserGroupType.WORKSPACE);
        List<Group> groups = groupMapper.selectList(groupQueryWrapper);
        List<String> groupIds = groups.stream().map(Group::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIds)) {
            return ResponseResult.success();
        }
        LambdaQueryWrapper<UserGroup> userGroup = new LambdaQueryWrapper<>();
        userGroup.eq(UserGroup::getUserId, userId).eq(UserGroup::getSourceId, workspaceId).in(UserGroup::getGroupId, groupIds);
        User user = baseMapper.selectById(userId);
        if (StringUtils.equals(workspaceId, user.getLastWorkspaceId())) {
            user.setLastWorkspaceId("");
            baseMapper.updateById(user);
        }
        userGroupMapper.delete(userGroup);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<String> deleteUser(String userId) {
        LambdaQueryWrapper<UserGroup> userGroupWrapper = new LambdaQueryWrapper<>();
        userGroupWrapper.eq(UserGroup::getUserId, userId);
        userGroupMapper.delete(userGroupWrapper);
        baseMapper.deleteById(userId);
        return ResponseResult.success("用户删除成功");
    }

    @Override
    public ResponseResult<String> updateUser(User user) {
        return null;
    }

    @Override
    public ResponseResult<Map<String, Object>> getProjectMemberList(QueryMemberRequest request, long page, long limit) {
        IPage<User> iPage = baseMapper.getProjectMemberList(new Page<>(page, limit), request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", iPage.getTotal());
        result.put("records", iPage.getRecords());
        return ResponseResult.success(result);
    }

    private List<String> getUserExistSourceGroup(String userId, String sourceId) {
        LambdaQueryWrapper<UserGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroup::getUserId, userId).eq(UserGroup::getSourceId, sourceId);
        List<UserGroup> userGroups = userGroupMapper.selectList(wrapper);
        return userGroups.stream().map(UserGroup::getGroupId).collect(Collectors.toList());
    }

    /**
     * 某项目/工作空间下能查看到的用户组
     *
     * @param type
     * @param sourceId
     * @return java.util.List<java.lang.String>
     */
    private List<String> getGroupIdsByType(String type, String sourceId) {
        List<String> scopeList = Arrays.asList("global", sourceId);
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Group::getScopeId, scopeList).eq(Group::getType, type);
        List<Group> groups = groupMapper.selectList(wrapper);
        return groups.stream().map(Group::getId).collect(Collectors.toList());
    }

    private List<Project> getProjectListByWsAndUserId(String sourceId, String userId) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getWorkspaceId, sourceId);
        List<Project> projects = projectMapper.selectList(wrapper);

        LambdaQueryWrapper<UserGroup> ug = new LambdaQueryWrapper<>();
        ug.eq(UserGroup::getUserId, userId);
        List<UserGroup> userGroups = userGroupMapper.selectList(ug);

        List<Project> projectList = new ArrayList<>();
        userGroups.forEach(userGroup -> {
            projects.forEach(project -> {
                if (StringUtils.equals(userGroup.getSourceId(), project.getId())) {
                    if (!projectList.contains(project)) {
                        projectList.add(project);
                    }
                }
            });
        });
        return projectList;
    }

}
