package io.square.controller;

import io.square.common.ResponseResult;
import io.square.entity.Project;
import io.square.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Resource
    ProjectService service;

    @PostMapping("/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getProjectList(@RequestBody Project project, @PathVariable long page, @PathVariable long limit) {
        return service.getProjectList(project, page, limit);
    }

    @PostMapping("/list/related")
    public ResponseResult<List<Project>> getUserProject(@RequestBody Project project) {
        return service.getUserProject(project);
    }

    @PostMapping("/add")
    public ResponseResult<Project> addProject(@RequestBody Project project) {
        return service.addProject(project);
    }

    @PostMapping("/update")
    public ResponseResult<Project> updateProject(@RequestBody Project project) {
        return service.updateProject(project);
    }

    @GetMapping("/delete/{projectId}")
    public ResponseResult<String> deleteProject(@PathVariable String projectId) {
        return service.deleteProject(projectId);
    }

    @GetMapping("/listAll")
    public ResponseResult<List<Project>> listAll() {
        return service.getProjectList();
    }
}
