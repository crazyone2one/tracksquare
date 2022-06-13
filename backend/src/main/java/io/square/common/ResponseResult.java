package io.square.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 统一数据返回类
 *
 * @author by 11's papa on 2022年06月13日
 * @version 1.0.0
 */
@Data
public class ResponseResult<T> implements Serializable {
    private Boolean success;
    private Integer code;
    private String message;
    private Date timestamp = new Date();
    private T data;

    /**
     * 请求成功
     *
     * @return cn.master.square.common.result.ResponseResult<E>
     */
    public static <E> ResponseResult<E> success() {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.SUCCESS.getSuccess());
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        return result;
    }

    public static <E> ResponseResult<E> success(String message) {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.SUCCESS.getSuccess());
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 请求成功，有返回结果
     *
     * @param data 返回结果
     * @return cn.master.square.common.result.ResponseResult<E>
     */
    public static <E> ResponseResult<E> success(E data) {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.SUCCESS.getSuccess());
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <E> ResponseResult<E> success(String message, E data) {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.SUCCESS.getSuccess());
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 请求失败
     *
     * @param data
     * @return cn.master.square.common.result.ResponseResult<E>
     */
    public static <E> ResponseResult<E> fail(E data) {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.FAILED.getSuccess());
        result.setCode(ResultEnum.FAILED.getCode());
        result.setMessage(ResultEnum.FAILED.getMessage());
        result.setData(data);
        return result;
    }

    public static <E> ResponseResult<E> fail(String message) {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.FAILED.getSuccess());
        result.setCode(ResultEnum.FAILED.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 请求失败
     *
     * @param message 返回字符串
     * @param data    返回内容
     * @return cn.master.square.common.result.ResponseResult<E>
     */
    public static <E> ResponseResult<E> fail(String message, E data) {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.FAILED.getSuccess());
        result.setCode(ResultEnum.FAILED.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <E> ResponseResult<E> fail(int code, String message, E data) {
        ResponseResult<E> result = new ResponseResult<>();
        result.setSuccess(ResultEnum.FAILED.getSuccess());
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
