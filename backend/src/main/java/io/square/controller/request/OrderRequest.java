package io.square.controller.request;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by 11's papa on 2022/6/27 0027
 * @version 1.0.0
 */
@Setter
public class OrderRequest {
    private String name;
    private String type;

    private String prefix;

    public String getName() {
        if (checkSqlInjection(name)) {
            return "1";
        }
        return name;
    }

    public String getType() {
        if (StringUtils.equalsIgnoreCase(type, "asc")) {
            return "ASC";
        } else {
            return "DESC";
        }
    }

    public String getPrefix() {
        if (checkSqlInjection(prefix)) {
            return "";
        }
        return prefix;
    }

    private static Pattern pattern = Pattern.compile("^\\w+$");

    public static boolean checkSqlInjection(String script) {
        if (StringUtils.isEmpty(script)) {
            return false;
        }
        Matcher matcher = pattern.matcher(script.toLowerCase());
        return !matcher.find();
    }
}
