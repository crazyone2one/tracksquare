package io.square.utils;

import java.util.function.BiFunction;

/**
 * @author by 11's papa on 2022/6/17 0017
 * @version 1.0.0
 */
public class ServiceUtils {
    public static final int ORDER_STEP = 5000;

    public static Long getNextOrder(String groupId, BiFunction<String, Long, Long> getLastOrderFunc) {
        Long lastOrder = getLastOrderFunc.apply(groupId, null);
        return (lastOrder == null ? 0 : lastOrder) + ServiceUtils.ORDER_STEP;
    }
}
