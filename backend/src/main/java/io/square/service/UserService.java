package io.square.service;

import io.square.common.ResponseResult;
import io.square.controller.request.AddMemberRequest;
import io.square.controller.request.QueryMemberRequest;
import io.square.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-13
 */
public interface UserService extends IService<User> {

    List<User> getUsersList();

    ResponseResult<Map<String, Object>> getPageList(User user, long page, long limit);

    ResponseResult<User> updateCurrentUser(User user);

    ResponseResult<List<User>> getProjectMember(QueryMemberRequest request);

    Map<String, User> queryNameByIds(List<String> userIds);

    ResponseResult<List<User>> getMemberList(User user);

    ResponseResult<List<User>> getUserList();

    ResponseResult<User> switchUserResource(String sourceId, String userId, String workspace);

    ResponseResult<Map<String, Object>> getMemberList(QueryMemberRequest request, long page, long limit);

    ResponseResult<String> addWorkspaceMember(AddMemberRequest request);

    void addGroupMember(String type, String sourceId, List<String> userIds, List<String> groupIds);

    /**
     * 移除工作区间下成员
     *
     * @param userId      待移除的成员id
     * @param workspaceId 工作区间id
     * @return io.square.common.ResponseResult<java.lang.String>
     */

    ResponseResult<String> deleteMember(String userId, String workspaceId);

    /**
     * 删除用户
     *
     * @param userId 待删除用户id
     * @return io.square.common.ResponseResult<java.lang.String>
     */

    ResponseResult<String> deleteUser(String userId);

    ResponseResult<String> updateUser(User user);

    ResponseResult<Map<String, Object>> getProjectMemberList(QueryMemberRequest request, long page, long limit);
}
