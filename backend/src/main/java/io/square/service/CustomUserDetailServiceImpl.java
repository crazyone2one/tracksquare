package io.square.service;

import io.square.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author by 11's papa on 2022/6/13 0013
 * @version 1.0.0
 */
@Slf4j
@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {
    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        io.square.entity.User byUsername = userMapper.findByUsername(username);
        if (Objects.nonNull(byUsername)) {
            return new User(byUsername.getName(), byUsername.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("user not found !!!");
        }
    }
}
