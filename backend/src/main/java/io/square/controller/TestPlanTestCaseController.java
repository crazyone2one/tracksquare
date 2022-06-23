package io.square.controller;

import io.square.common.ResponseResult;
import io.square.controller.request.QueryTestPlanCaseRequest;
import io.square.entity.TestPlanTestCase;
import io.square.service.TestPlanTestCaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 测试计划-测试用例关联 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-22
 */
@RestController
@RequestMapping("/test/plan/case")
public class TestPlanTestCaseController {
    @Resource
    TestPlanTestCaseService service;

    @PostMapping("/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getTestPlanCases(@RequestBody QueryTestPlanCaseRequest request,
                                                                @PathVariable long limit, @PathVariable long page) {
        return service.pageList(request, page, limit);
    }

    @PostMapping("/edit")
    public ResponseResult<String> editTestCase(@RequestBody TestPlanTestCase request) {
        return service.editTestCase(request);
    }
}
