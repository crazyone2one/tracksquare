package io.square.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.square.config.JwtSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author created by 11's papa on 2022/6/12-17:41
 * @version 1.0.0
 */
@Slf4j
@Component
public class JwtTokenUtils {

    private final JwtSecurityProperties jwtSecurityProperties;

    public JwtTokenUtils(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    /**
     * 创建Token
     *
     * @param map
     * @return
     */
    public String generateToken(Map<String, Object> map) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * jwtSecurityProperties.getExpirationTime());
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(map)
                .setSubject(map.get("userName").toString())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecurityProperties.getBase64Secret())
                .compact();
    }

    public String generateToken(String username) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * jwtSecurityProperties.getExpirationTime());
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                // 签发人（iss）：荷载部分的标准字段之一，代表这个 JWT 的所有者。通常是 username、userid 这样具有用户代表性的内容。
                .setSubject(username)
                // 签发时间（iat）：荷载部分的标准字段之一，代表这个 JWT 的生成时间。
                .setIssuedAt(nowDate)
                // 过期时间（exp）：荷载部分的标准字段之一，代表这个 JWT 的有效期。
                .setExpiration(expireDate)
                // 设置生成签名的算法和秘钥
                .signWith(SignatureAlgorithm.HS512, jwtSecurityProperties.getBase64Secret())
                .compact();
    }

    public String getUserNameByToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecurityProperties.getBase64Secret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    private Claims getTokenClaim(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(jwtSecurityProperties.getBase64Secret())
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
            log.error(e.getMessage());
        }
        return claims;
    }

    public boolean isTokenExpired(String token) {
        return getTokenClaim(token).getExpiration().before(new Date());
    }
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }
}

