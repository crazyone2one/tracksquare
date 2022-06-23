package io.square.controller;

import io.square.common.ResponseResult;
import io.square.controller.request.QueryTestCaseRequest;
import io.square.entity.TestCase;
import io.square.service.TestCaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
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
        request.setId(UUID.randomUUID().toString());
        return service.addTestCase(request);
    }

    @GetMapping("/get/{testCaseId}")
    public ResponseResult<TestCase> getTestCase(@PathVariable String testCaseId) {
        return service.getTestCase(testCaseId);
    }

    @PostMapping(value = "/add")
    public ResponseResult<TestCase> addTestCase(@RequestBody TestCase request) {
        if (StringUtils.isBlank(request.getId())) {
            //新增 后端生成 id
            request.setId(UUID.randomUUID().toString());
        }
        return service.saveCase(request);
    }

    @PostMapping("/relate/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getTestCaseRelateList(@PathVariable long limit, @PathVariable long page, @RequestBody QueryTestCaseRequest request) {
        return service.getTestCaseRelateList(page, limit, request);
    }
}
