package io.square.controller;

import io.square.common.ResponseResult;
import io.square.dto.WorkspaceResource;
import io.square.entity.Workspace;
import io.square.service.WorkspaceService;
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
 * @since 2022-06-27
 */
@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    @Resource
    WorkspaceService service;

    @PostMapping("add")
    public ResponseResult<Workspace> addWorkspace(@RequestBody Workspace workspace) {
        return service.saveWorkspace(workspace);
    }

    @PostMapping("special/add")
    public ResponseResult<Workspace> addWorkspaceByAdmin(@RequestBody Workspace workspace) {
        return service.addWorkspaceByAdmin(workspace);
    }

    @PostMapping("list/all/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getAllWorkspaceList(@PathVariable long limit, @PathVariable long page, @RequestBody Workspace workspace) {
        return service.getAllWorkspaceList(workspace, page, limit);
    }

    @PostMapping("update")
    public ResponseResult<Workspace> updateWorkspace(@RequestBody Workspace workspace) {
        return service.saveWorkspace(workspace);
    }

    @GetMapping("/list")
    public ResponseResult<List<Workspace>> getWorkspaceList() {
        return service.getWorkspaceList();
    }

    @GetMapping("/list/resource/{groupId}/{type}")
    public ResponseResult<WorkspaceResource> listResource(@PathVariable String groupId, @PathVariable String type) {
        return service.listResource(groupId, type);
    }

    @GetMapping("/list/userworkspace/{userId}")
    public ResponseResult<List<Workspace>> getWorkspaceListByUserId(@PathVariable String userId) {
        return service.getWorkspaceListByUserId(userId);
    }
}
