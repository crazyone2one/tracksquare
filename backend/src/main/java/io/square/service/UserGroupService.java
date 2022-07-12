package io.square.service;

import io.square.common.ResponseResult;
import io.square.dto.GroupPermissionDTO;
import io.square.entity.Group;
import io.square.entity.UserGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
public interface UserGroupService extends IService<UserGroup> {

    ResponseResult<List<Group>> getWorkspaceMemberGroups(String workspaceId, String userId);

    ResponseResult<Map<String, Object>> getGroupUser(Group group, long page, long limit);

    ResponseResult<List<?>> getGroupSource(String groupId, String userId);

    ResponseResult<List<Map<String, Object>>> getAllUserGroup(String userId);

    ResponseResult<List<Group>> getGroupsByType(Group request);

    ResponseResult<List<Group>> getGroupByType(Group request);

    ResponseResult<GroupPermissionDTO> getGroupResource(Group group);

    ResponseResult<List<Group>> getProjectMemberGroups(String projectId, String userId);
}
