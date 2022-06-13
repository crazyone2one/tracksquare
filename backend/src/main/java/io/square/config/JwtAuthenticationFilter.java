package io.square.config;

import io.square.service.CustomUserDetailServiceImpl;
import io.square.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author by 11's papa on 2022/6/13 0013
 * @version 1.0.0
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private CustomUserDetailServiceImpl userDetailService;
    @Autowired
    private JwtSecurityProperties jwtSecurityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        String username= null;
        String jwtToken;
        if (Objects.nonNull(requestTokenHeader) && requestTokenHeader.startsWith(jwtSecurityProperties.getTokenPrefix())) {
            jwtToken = requestTokenHeader.substring(7);
            try{
                username = jwtTokenUtils.getUsernameFromToken(jwtToken);
            }catch(Exception e){
                e.printStackTrace();
            }
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if (Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                log.info("Token is not validated");
            }
        }
        filterChain.doFilter(request, response);
    }
}
