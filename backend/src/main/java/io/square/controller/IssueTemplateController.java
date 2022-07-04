package io.square.controller;

import io.square.common.ResponseResult;
import io.square.entity.IssueTemplate;
import io.square.service.IssueTemplateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-27
 */
@RestController
@RequestMapping("/field/template/issue")
public class IssueTemplateController {

    @Resource
    IssueTemplateService service;

    @GetMapping("/get/relate/{projectId}")
    public ResponseResult<IssueTemplate> getTemplate(@PathVariable String projectId) {
        return service.getTemplate(projectId);
    }
}
