package io.square.controller;

import io.square.common.ResponseResult;
import io.square.controller.request.QueryCustomFieldRequest;
import io.square.entity.CustomField;
import io.square.service.CustomFieldService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/custom/field")
public class CustomFieldController {

    @Resource
    CustomFieldService service;

    @PostMapping("/add")
    public ResponseResult<CustomField> add(@RequestBody CustomField customField) {
        return service.add(customField);
    }

    @PostMapping("/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> listPage(@RequestBody QueryCustomFieldRequest request, @PathVariable Long limit, @PathVariable Long page) {
        return service.listPageData(request, page, limit);
    }
}
