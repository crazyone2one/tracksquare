package io.square.controller;

import io.square.common.ResponseResult;
import io.square.controller.request.QueryMemberRequest;
import io.square.entity.User;
import io.square.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
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
}
