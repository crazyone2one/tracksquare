package io.square.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.square.common.ResponseResult;
import io.square.controller.request.PlanCaseRelevanceRequest;
import io.square.entity.TestPlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
public interface TestPlanService extends IService<TestPlan> {
    /**
     * 获取测试阶段数据
     *
     * @param projectId 项目id
     * @return io.square.common.ResponseResult<com.fasterxml.jackson.databind.JsonNode>
     */

    ResponseResult<JsonNode> getStageOption(String projectId);

    /**
     * 保存
     *
     * @param testPlan 参数
     * @return io.square.common.ResponseResult<io.square.entity.TestPlan>
     */
    ResponseResult<TestPlan> addTestPlan(TestPlan testPlan);

    /**
     * 分页查询
     *
     * @param request 参数
     * @param page    当前页数
     * @param limit   每页显示数量
     * @return io.square.common.ResponseResult<java.util.Map < java.lang.String, java.lang.Object>>
     */
    ResponseResult<Map<String, Object>> pageList(TestPlan request, Long page, Long limit);

    /**
     * 查询列表
     *
     * @param request 参数
     * @return io.square.common.ResponseResult<java.util.List < io.square.entity.TestPlan>>
     */
    ResponseResult<List<TestPlan>> listTestAllPlan(TestPlan request);

    /**
     * 根据id查询
     *
     * @param testPlanId id
     * @return io.square.common.ResponseResult<io.square.entity.TestPlan>
     */
    ResponseResult<TestPlan> getTestPlan(String testPlanId);

    /**
     * 保存关联数据
     *
     * @param request 参数
     * @return io.square.common.ResponseResult<java.lang.String>
     */
    ResponseResult<String> testPlanRelevance(PlanCaseRelevanceRequest request);

    ResponseResult<String> editTestPlanStatus(String planId);

    List<String> getPlanIdByProjectId(String projectId);

    int deleteTestPlan(String planId);
}
