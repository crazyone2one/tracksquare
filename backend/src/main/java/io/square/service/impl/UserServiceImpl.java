package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.controller.request.QueryMemberRequest;
import io.square.entity.Project;
import io.square.entity.User;
import io.square.entity.UserGroup;
import io.square.mapper.ProjectMapper;
import io.square.mapper.UserGroupMapper;
import io.square.mapper.UserMapper;
import io.square.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    ProjectMapper projectMapper;
    @Resource
    UserGroupMapper userGroupMapper;

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

        return projectList/**/;
    }

}
