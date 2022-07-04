package io.square.service;

import io.square.common.ResponseResult;
import io.square.entity.Group;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-28
 */
public interface GroupService extends IService<Group> {

    ResponseResult<Group> addGroup(Group group);

    ResponseResult<String> editGroup(Group group);

    ResponseResult<Map<String, Object>> getGroupList(Group group, long page, long limit);
}
