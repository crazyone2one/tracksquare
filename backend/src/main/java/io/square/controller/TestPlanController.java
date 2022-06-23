package io.square.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.square.common.ResponseResult;
import io.square.controller.request.PlanCaseRelevanceRequest;
import io.square.entity.TestPlan;
import io.square.service.TestPlanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 测试计划
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/test/plan")
public class TestPlanController {
    @Resource
    TestPlanService service;

    @GetMapping("/get/stage/option/{projectId}")
    public ResponseResult<JsonNode> getStageOption(@PathVariable String projectId) {
        return service.getStageOption(projectId);
    }

    @PostMapping("/add")
    public ResponseResult<TestPlan> addTestPlan(@RequestBody TestPlan testPlan) {
        return service.addTestPlan(testPlan);
    }

    @PostMapping("/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> list(@PathVariable long page, @PathVariable long limit, @RequestBody TestPlan request) {
        return service.pageList(request, page, limit);
    }

    @PostMapping("/list/all")
    public ResponseResult<List<TestPlan>> listAll(@RequestBody TestPlan request) {
        return service.listTestAllPlan(request);
    }

    @PostMapping("/get/{testPlanId}")
    public ResponseResult<TestPlan> getTestPlan(@PathVariable String testPlanId) {
        return service.getTestPlan(testPlanId);
    }

    @PostMapping("/relevance")
    public ResponseResult<String> testPlanRelevance(@RequestBody PlanCaseRelevanceRequest request) {
        return service.testPlanRelevance(request);
    }

    @PostMapping("/edit/status/{planId}")
    public ResponseResult<String> editTestPlanStatus(@PathVariable String planId) {
        return service.editTestPlanStatus(planId);
    }
}
