package io.square.common;

import lombok.Getter;

/**
 * 统一响应体的响应状态码枚举类
 *
 * @author by 11's papa on 2022年06月13日
 * @version 1.0.0
 */
@Getter
public enum ResultEnum {
    /**
     *
     */
    SUCCESS(true, 200, "请求成功"),
    FAILED(false, 400, "请求失败"),
    ERROR(false, 400, "未知错误"),
    BAD_CREDENTIAL(false, 401, "认证失败"),
    NOT_FOUND(false, 404, "找不到请求的资源"),
    Internal_Server_Error(false, 500, "服务器内部错误，无法完成请求");

    private final Boolean success;
    private final Integer code;
    private final String message;

    ResultEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
