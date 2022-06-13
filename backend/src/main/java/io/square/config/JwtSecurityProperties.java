package io.square.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author created by 11's papa on 2022/6/12-17:25
 * @version 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtSecurityProperties {

    /** Request Headers ：Authorization */
    private String authorization;

    /** 令牌前缀，最后留个空格 Bearer */
    private String tokenPrefix;

    /** Base64对该令牌进行编码 */
    private String base64Secret;

    /** 令牌过期时间 此处单位/毫秒 */
    private Long expirationTime;

    private Long expirationRemember;
}
