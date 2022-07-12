package io.square.controller;

import io.square.common.ResponseResult;
import io.square.dto.GroupPermissionDTO;
import io.square.entity.Group;
import io.square.service.GroupService;
import io.square.service.UserGroupService;
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
@RequestMapping("/user/group")
public class UserGroupController {
    @Resource
    UserGroupService service;
    @Resource
    GroupService groupService;

    @GetMapping("/list/ws/{workspaceId}/{userId}")
    public ResponseResult<List<Group>> getWorkspaceMemberGroups(@PathVariable String userId, @PathVariable String workspaceId) {
        return service.getWorkspaceMemberGroups(workspaceId, userId);
    }

    @PostMapping("/get/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getGroupList(@PathVariable long page, @PathVariable long limit, @RequestBody Group group) {
        return groupService.getGroupList(group, page, limit);
    }

    @PostMapping("/add")
    public ResponseResult<Group> addGroup(@RequestBody Group group) {
        return groupService.addGroup(group);
    }

    @PostMapping("edit")
    public ResponseResult<String> editGroup(@RequestBody Group group) {
        return groupService.editGroup(group);
    }

    @PostMapping("/user/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getGroupUser(@RequestBody Group group, @PathVariable long page, @PathVariable long limit) {
        return service.getGroupUser(group, page, limit);
    }

    @GetMapping("/source/{userId}/{groupId}")
    public ResponseResult<List<?>> getGroupSource(@PathVariable String groupId, @PathVariable String userId) {
        return service.getGroupSource(groupId, userId);
    }

    @GetMapping("/all/{userId}")
    public ResponseResult<List<Map<String, Object>>> getAllUserGroup(@PathVariable String userId) {
        return service.getAllUserGroup(userId);
    }

    @PostMapping("/list")
    public ResponseResult<List<Group>> getGroupsByType(@RequestBody Group request) {
        return service.getGroupsByType(request);
    }
    @PostMapping("/get")
    public ResponseResult<List<Group>> getGroupByType(@RequestBody Group request) {
        return service.getGroupByType(request);
    }

    @PostMapping("/permission")
    public ResponseResult<GroupPermissionDTO> getGroupResource(@RequestBody Group group) {
        return service.getGroupResource(group);
    }

    @GetMapping("/list/project/{projectId}/{userId}")
    public ResponseResult<List<Group>> getProjectMemberGroups(@PathVariable String projectId, @PathVariable String userId) {
        return service.getProjectMemberGroups(projectId, userId);
    }
}
