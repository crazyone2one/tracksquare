package io.square.mapper;

import io.square.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    @Select("select MAX(system_id) FROM project")
    String getMaxSystemId();
}
