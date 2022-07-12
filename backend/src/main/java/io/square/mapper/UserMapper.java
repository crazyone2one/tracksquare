package io.square.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.square.controller.request.QueryMemberRequest;
import io.square.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询
     *
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

    @Update("update user set last_workspace_id = #{workspaceId} where id = #{userId} and (last_workspace_id is null or last_workspace_id = '')")
    void updateLastWorkspaceIdIfNull(@Param("workspaceId") String workspaceId, @Param("userId") String userId);

    List<User> getMemberList(@Param("member") User user);

    IPage<User> getMemberList(Page<User> page, @Param("member") QueryMemberRequest request);
    @Select("SELECT DISTINCT * FROM ( SELECT `user`.* FROM user_group JOIN `user` ON user_group.user_id = `user`.id WHERE user_group.source_id = #{request.projectId} order by `user`.update_time desc) temp")
    IPage<User> getProjectMemberList(Page<User> page, @Param("member") QueryMemberRequest request);

    @Update("update user set last_project_id = #{projectId} where id = #{userId} and (last_project_id is null or last_project_id = '')")
    void updateLastProjectIdIfNull(@Param("projectId") String projectId, @Param("userId") String userId);
}
