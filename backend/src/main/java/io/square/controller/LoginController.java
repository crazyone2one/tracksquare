package io.square.controller;

import io.square.entity.User;
import io.square.mapper.UserMapper;
import io.square.model.JwtRequest;
import io.square.service.CustomUserDetailServiceImpl;
import io.square.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    private CustomUserDetailServiceImpl customUserDetailService;
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Resource
    UserMapper userMapper;


    @PostMapping("/login")
    public String loginAction(@RequestBody JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        UserDetails userDetails = customUserDetailService.loadUserByUsername(jwtRequest.getUserName());
        String token = jwtTokenUtils.generateToken(userDetails, false);
        log.info(token);
        return token;
    }

    @PostMapping("/reg")
    public User register(@RequestBody JwtRequest jwtRequest) {
        User build = User.builder().name(jwtRequest.getUserName()).build();
        userMapper.insert(build);
        return build;
    }
}
