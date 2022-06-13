package io.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.square.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("select  * from `user` where name=#{username}")
    User findByUsername(String username);
}
