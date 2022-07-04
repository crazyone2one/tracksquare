package io.square.controller;

import io.square.common.ResponseResult;
import io.square.entity.CustomFieldTemplate;
import io.square.service.CustomFieldTemplateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@RestController
@RequestMapping("/custom/field/template")
public class CustomFieldTemplateController {

    @Resource
    CustomFieldTemplateService service;

    @PostMapping("/list")
    public ResponseResult<List<CustomFieldTemplate>> list(@RequestBody CustomFieldTemplate request) {
        return service.pageList(request);
    }
}
