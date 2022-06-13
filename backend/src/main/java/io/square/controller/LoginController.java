package io.square.controller;

import io.square.common.ResponseResult;
import io.square.entity.User;
import io.square.mapper.UserMapper;
import io.square.model.JwtRequest;
import io.square.service.UserService;
import io.square.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by 11's papa on 2022年06月13日
 * @version 1.0.0
 */
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;


    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> loginAction(@RequestBody JwtRequest jwtRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword());
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("用户名/密码错误");
        }
        User byUsername = userMapper.findByUsername(jwtRequest.getUserName());
        String token = jwtTokenUtils.generateToken(jwtRequest.getUserName(), false);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", token);
        result.put("user", byUsername);
        return ResponseResult.success(result);
    }

    @GetMapping("/user/list")
    public ResponseResult<List<User>> register() {
        List<User> usersList = userService.getUsersList();
        return ResponseResult.success(usersList);
    }

}
