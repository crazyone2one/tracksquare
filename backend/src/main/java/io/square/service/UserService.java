package io.square.service;

import io.square.common.ResponseResult;
import io.square.controller.request.QueryMemberRequest;
import io.square.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
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
}
