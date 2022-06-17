package io.square.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.square.entity.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     *
    根据用户名查询
 * @param username
 * @return io.square.entity.User
     */

    @Select("select  * from `user` where name=#{username}")
    User findByUsername(String username);

    @Select("select * from `user` ${ew.customSqlSegment}")
    IPage<User> queryAll(@Param(Constants.WRAPPER) LambdaQueryWrapper<User> wrapper);
    @MapKey("id")
    @Select("select id, name from `user` ${ew.customSqlSegment}")
    Map<String, User> queryNameByIds(@Param(Constants.WRAPPER) LambdaQueryWrapper<User> wrapper);
}
