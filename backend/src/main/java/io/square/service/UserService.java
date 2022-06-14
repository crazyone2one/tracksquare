package io.square.service;

import io.square.common.ResponseResult;
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
}
