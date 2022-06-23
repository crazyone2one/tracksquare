package io.square.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.square.entity.TestCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
@Mapper
public interface TestCaseMapper extends BaseMapper<TestCase> {
    @Select("select node_id AS moduleId,count(id) AS countNum from test_case ${ew.customSqlSegment}")
    List<Map<String, Object>> moduleCountByCollection(@Param(Constants.WRAPPER) LambdaQueryWrapper<TestCase> wrapper);

    @Select(" select test_case.id, test_case.node_id, test_case.node_path from test_case ${ew.customSqlSegment}")
    List<TestCase> getForNodeEdit(@Param(Constants.WRAPPER) LambdaQueryWrapper<TestCase> wrapper);

    @Select(" select id from test_case ${ew.customSqlSegment}")
    List<String> selectIdsByNodeIds(@Param(Constants.WRAPPER) LambdaQueryWrapper<TestCase> wrapper);

    @Select("select * from test_case ${ew.customSqlSegment}")
    TestCase getMaxNumByProjectId(@Param(Constants.WRAPPER) LambdaQueryWrapper<TestCase> wrapper);

    Long getLastOrder(@Param("projectId") String projectId, @Param("baseOrder") Long baseOrder);

    @Select("select * from test_case ${ew.customSqlSegment}")
    IPage<TestCase> getTestCaseByNotInPlan(IPage<?> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<TestCase> wrapper);

    /**
     * 根据plan id查询对应测试用例的项目、模块信息
     *
     * @param planId plan id
     * @return java.util.List<io.square.entity.TestCase>
     */
    @Select("select tc.project_id, tc.node_id from test_plan_test_case tptc join test_case tc on tptc.case_id = tc.id where tptc.plan_id = #{planId}")
    List<TestCase> getTestCaseWithNodeInfo(@Param("planId") String planId);
}
