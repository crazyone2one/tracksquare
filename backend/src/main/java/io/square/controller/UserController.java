package io.square.controller;

import io.square.common.ResponseResult;
import io.square.controller.request.AddMemberRequest;
import io.square.controller.request.QueryMemberRequest;
import io.square.entity.User;
import io.square.service.CheckPermissionService;
import io.square.service.UserService;
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
 * @since 2022-06-13
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    CheckPermissionService checkPermissionService;

    @PostMapping("/list/all/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getPageList(@PathVariable long limit, @PathVariable long page, @RequestBody User user) {
        return userService.getPageList(user, page, limit);
    }

    @PostMapping("/update/current")
    public ResponseResult<User> updateCurrentUser(@RequestBody User user) {
        return userService.updateCurrentUser(user);
    }

    @PostMapping("/project/member/tester/list")
    public ResponseResult<List<User>> getProjectMember(@RequestBody QueryMemberRequest request) {
        return userService.getProjectMember(request);
    }

    @PostMapping("/special/ws/member/list/all")
    public ResponseResult<List<User>> getMemberListByAdmin(@RequestBody User user) {
        return userService.getMemberList(user);
    }

    @GetMapping("/list")
    public ResponseResult<List<User>> getUserList() {
        return userService.getUserList();
    }

    @PostMapping("/switch/source/ws/{sourceId}/{userId}")
    public ResponseResult<User> switchWorkspace(@PathVariable String sourceId, @PathVariable String userId) {
        return userService.switchUserResource(sourceId, userId, "workspace");
    }

    @PostMapping("/special/ws/member/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getMemberListByAdmin(@RequestBody QueryMemberRequest request, @PathVariable long limit, @PathVariable long page) {
        return userService.getMemberList(request, page, limit);
    }

    @PostMapping("/ws/member/list/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getMemberList(@RequestBody QueryMemberRequest request, @PathVariable long limit, @PathVariable long page) {
        return userService.getMemberList(request, page, limit);
    }

    @PostMapping("/special/ws/member/add")
    public ResponseResult<String> addMemberByAdmin(@RequestBody AddMemberRequest request) {
        return userService.addWorkspaceMember(request);
    }

    /**
     * 移除工作区间下成员
     *
     * @param userId      待移除的成员id
     * @param workspaceId 工作区间id
     * @return io.square.common.ResponseResult<java.lang.String>
     */
    @GetMapping("/special/ws/member/delete/{workspaceId}/{userId}")
    public ResponseResult<String> deleteMemberByAdmin(@PathVariable String userId, @PathVariable String workspaceId) {
        return userService.deleteMember(userId, workspaceId);
    }

    @GetMapping("/ws/member/delete/{workspaceId}/{userId}")
    public ResponseResult<String> deleteMember(@PathVariable String userId, @PathVariable String workspaceId) {
        return userService.deleteMember(userId, workspaceId);
    }

    @GetMapping("/special/delete/{userId}")
    public ResponseResult<String> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }

    @PostMapping("/special/update")
    public ResponseResult<String> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/ws/project/member/list/{workspaceId}/{page}/{limit}")
    public ResponseResult<Map<String, Object>> getProjectMemberListForWorkspace(@RequestBody QueryMemberRequest request, @PathVariable long limit, @PathVariable long page, @PathVariable String workspaceId) {
        checkPermissionService.checkProjectBelongToWorkspace(request.getProjectId(), workspaceId);
        return userService.getProjectMemberList(request, page, limit);
    }

}
