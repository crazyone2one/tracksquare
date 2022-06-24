package io.square.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.square.controller.request.QueryTestPlanCaseRequest;
import io.square.dto.CountMapDTO;
import io.square.dto.ParamsDTO;
import io.square.dto.TestPlanCaseDTO;
import io.square.entity.TestPlanTestCase;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 测试计划-测试用例关联 Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-22
 */
@Mapper
public interface TestPlanTestCaseMapper extends BaseMapper<TestPlanTestCase> {

    @Select("select `order` from test_plan_test_case where plan_id = #{planId} and `order` > #{baseOrder} order by `order` desc limit 1;")
    Long getLastOrder(@Param("planId") String planId, @Param("baseOrder") Long baseOrder);

    @MapKey("id")
    Map<String, ParamsDTO> testPlanTestCaseCount(@Param("planIds") Set<String> planIds);

    @Select("select status as `key`, count(*) as `value` from test_plan_test_case where plan_id = #{planId} and is_del = 0 group by status")
    List<CountMapDTO> getExecResultMapByPlanId(@Param("planId") String planId);

    @Select("select `status` from test_plan_test_case where plan_id = #{planId}")
    List<String> getStatusByPlanId(String planId);

    IPage<TestPlanCaseDTO> list(@Param("request") QueryTestPlanCaseRequest request, @Param("objectPage") Page<Object> objectPage);
}
