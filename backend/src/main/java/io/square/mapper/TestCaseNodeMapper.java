package io.square.mapper;

import io.square.entity.TestCaseNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
@Mapper
public interface TestCaseNodeMapper extends BaseMapper<TestCaseNode> {

    @Select("select * from test_case_node where test_case_node.project_id = #{projectId} order by pos asc")
    List<TestCaseNode> getNodeTreeByProjectId(@Param("projectId") String projectId);
}
