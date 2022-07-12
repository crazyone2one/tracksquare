package io.square.utils;

import io.square.entity.User;
import io.square.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author by 11's papa on 2022/6/21 0021
 * @version 1.0.0
 */
@Slf4j
@Component
public class SessionUtils {

    @Autowired
    UserMapper userMapper;

    public  String getCurrentProjectId() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        log.info("PROJECT: {}",request.getHeader("project"));
        if (request.getHeader("project") != null) {
            return request.getHeader("project");
        }
        User byUsername = userMapper.findByUsername(request.getUserPrincipal().getName());
        return byUsername.getLastProjectId();
    }

    public  String getCurrentWorkspaceId() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        log.info("PROJECT: {}",request.getHeader("workspace"));
        if (request.getHeader("workspace") != null) {
            return request.getHeader("workspace");
        }
        User byUsername = userMapper.findByUsername(request.getUserPrincipal().getName());
        return byUsername.getLastWorkspaceId();
    }

    public String getUserId() {
        return getUser().getId();
    }

    public User getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        return userMapper.findByUsername(request.getUserPrincipal().getName());
    }
}
