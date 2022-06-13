package io.square.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.square.config.JwtSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author created by 11's papa on 2022/6/12-17:41
 * @version 1.0.0
 */
@Slf4j
@Component
public class JwtTokenUtils implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    private final JwtSecurityProperties jwtSecurityProperties;

    public JwtTokenUtils(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    /**
     * 创建Token
     * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     *
     * @param claims  claims
     * @param subject subject
     * @return java.lang.String
     */
    public String generateToken(Map<String, Object> claims, String subject, boolean isRememberMe) {
        long expiration = isRememberMe ? jwtSecurityProperties.getExpirationRemember() : jwtSecurityProperties.getExpirationTime();
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expiration);
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                // 签发时间（iat）：荷载部分的标准字段之一，代表这个 JWT 的生成时间。
                .setIssuedAt(nowDate)
                // 过期时间（exp）：荷载部分的标准字段之一，代表这个 JWT 的有效期。
                .setExpiration(expireDate)
                // 设置生成签名的算法和秘钥
                .signWith(SignatureAlgorithm.HS512, jwtSecurityProperties.getBase64Secret())
                .compact();
        return jwtSecurityProperties.getTokenPrefix() + " " + token;
    }

    /**
     * generate token for user name
     *
     * @param username username
     * @return java.lang.String
     */
    public String generateToken(String username, boolean isRememberMe) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username, isRememberMe);
    }

    /**
     * generate token for user
     *
     * @param userDetails UserDetails
     * @return java.lang.String
     */
    public String generateToken(UserDetails userDetails, boolean isRememberMe) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, userDetails.getUsername(), isRememberMe);
    }

    /**
     * retrieve username from jwt token
     *
     * @param token token
     * @return java.lang.String
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Claims getAllClaimsFromToken(String token) {
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

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * check if the token has expired
     *
     * @param token token
     * @return boolean
     */
    public boolean isTokenExpired(String token) {
        final Date dateFromToken = getExpirationDateFromToken(token);
        return dateFromToken.before(new Date());
    }

    /**
     * retrieve expiration date from jwt token
     *
     * @param token token
     * @return java.util.Date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * validate token
     *
     * @param token       token
     * @param userDetails userDetails
     * @return boolean
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

