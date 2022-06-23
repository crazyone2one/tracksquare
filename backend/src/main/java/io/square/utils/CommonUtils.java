package io.square.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author by 11's papa on 2022/6/23 0023
 * @version 1.0.0
 */
public class CommonUtils {
    public static double getPercentWithDecimal(double value) {
        return new BigDecimal(value * 100).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
    }
}
