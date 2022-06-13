package io.square.model;

import lombok.Data;

/**
 * @author by 11's papa on 2022/6/13 0013
 * @version 1.0.0
 */
@Data
public class JwtRequest {
    String userName;
    String password;
}
