package io.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.square.entity.Group;
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
 * @since 2022-06-28
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {
    List<UserGroup> getUserGroup(@Param("userId") String userId, @Param("projectId") String projectId);

    IPage<Group> getGroupList(Page<Group> page, @Param("request") Group request);

    @Select("select r.id, r.name from workspace w join user_group ur on w.id = ur.source_id join `group` r on r.id = ur.group_id where w.id = #{workspaceId} and ur.user_id = #{userId}")
    List<Group> getWorkspaceMemberGroups(@Param("workspaceId") String workspaceId, @Param("userId") String userId);
}
