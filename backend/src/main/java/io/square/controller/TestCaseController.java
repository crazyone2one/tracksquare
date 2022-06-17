package io.square.controller;

import io.square.common.ResponseResult;
import io.square.controller.request.QueryTestCaseRequest;
import io.square.entity.TestCase;
import io.square.service.TestCaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/test/case")
public class TestCaseController {

    @Resource
    TestCaseService service;

    @PostMapping("/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> listCase(@PathVariable long limit, @PathVariable long page, @RequestBody QueryTestCaseRequest request) {
        return service.listTestCase(request, page, limit);
    }

    @PostMapping("/save")
    public ResponseResult<TestCase> saveCase(@RequestBody TestCase request) {
        return service.addTestCase(request);
    }
}
