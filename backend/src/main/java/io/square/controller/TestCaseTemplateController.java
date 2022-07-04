package io.square.controller;

import io.square.common.ResponseResult;
import io.square.controller.request.BaseQueryRequest;
import io.square.entity.TestCaseTemplate;
import io.square.service.TestCaseTemplateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@RestController
@RequestMapping("/field/template/case")
public class TestCaseTemplateController {
    @Resource
    TestCaseTemplateService service;

    @PostMapping("/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> listPageCaseTemplate(@PathVariable Long limit, @PathVariable Long page
            , @RequestBody BaseQueryRequest request) {
        return service.pageList(request, page, limit);
    }

    @PostMapping("/add")
    public ResponseResult<String> add(@RequestBody TestCaseTemplate request) {
        return service.add(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseResult<String> delete(@PathVariable String id) {
        return service.delete(id);
    }
}
