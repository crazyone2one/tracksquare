package io.square;

import io.square.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApplicationTests {
    @Resource
    JwtTokenUtils jwtTokenUtils;
    @Test
    void contextLoads() {

        String token = jwtTokenUtils.generateToken("lijinglin");
        System.out.println(token);
        System.out.println(jwtTokenUtils.getUserNameByToken(token));
    }

}
