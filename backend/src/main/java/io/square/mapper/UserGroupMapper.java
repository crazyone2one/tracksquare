package io.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.square.dto.RelatedSource;
import io.square.entity.Group;
import io.square.entity.User;
import io.square.entity.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroup> {

    IPage<User> getGroupUser(Page<User> userPage, @Param("request") Group request);

    List<RelatedSource> getRelatedSource(@Param("userId") String userId);
}
