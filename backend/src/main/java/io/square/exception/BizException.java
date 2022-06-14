package io.square.exception;

/**
 * @author by 11's papa on 2022/6/14 0014
 * @version 1.0.0
 */
public class BizException extends RuntimeException{
    private BizException(String message) {
        super(message);
    }

    private BizException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new BizException(message);
    }

    public static BizException getException(String message) {
        throw new BizException(message);
    }

    public static void throwException(Throwable t) {
        throw new BizException(t);
    }
}
