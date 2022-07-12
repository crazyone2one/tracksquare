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
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroup> {

    IPage<User> getGroupUser(Page<User> userPage, @Param("request") Group request);

    List<RelatedSource> getRelatedSource(@Param("userId") String userId);

    @Select("select r.id, r.name from project p join user_group ur on p.id = ur.source_id join `group` r on r.id = ur.group_id where p.id = #{projectId} and ur.user_id = #{userId}")
    List<Group> getProjectMemberGroups(@Param("projectId") String projectId, @Param("userId") String userId);
}
