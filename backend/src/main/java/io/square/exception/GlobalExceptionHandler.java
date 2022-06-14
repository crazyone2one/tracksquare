package io.square.exception;

import io.square.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * @author by 11's papa on 2022/6/13 0013
 * @version 1.0.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseResult<String> badCredentialsExceptionHandle(BadCredentialsException exception) {
        return ResponseResult.fail(HttpServletResponse.SC_FORBIDDEN,exception.getMessage(),null);
    }

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseResult<String> bizExceptionHandle(BizException exception) {
        return ResponseResult.fail(HttpServletResponse.SC_BAD_REQUEST,exception.getMessage(),null);
    }
}
