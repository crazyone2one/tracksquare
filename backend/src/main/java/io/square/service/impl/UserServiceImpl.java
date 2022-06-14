package io.square.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.square.common.ResponseResult;
import io.square.entity.User;
import io.square.mapper.UserMapper;
import io.square.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

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
        result.put("total",userPage.getTotal());
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

}
