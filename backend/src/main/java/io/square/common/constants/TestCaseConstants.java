package io.square.common.constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by 11's papa on 2022/6/15 0015
 * @version 1.0.0
 */
public class TestCaseConstants {
    public static final int MAX_NODE_DEPTH = 8;
    public enum Type {
        // 测试类型
        Functional("functional"), Performance("performance"), Aapi("api");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static List<String> getValues() {
            List<Type> types = Arrays.asList(Type.values());
            return  types.stream().map(Type::getValue).collect(Collectors.toList());
        }
    }

    public enum Method {
        // 测试方法
        Manual("manual"), Auto("auto");

        private String value;

        Method(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static List<String> getValues() {
            List<Method> types = Arrays.asList(Method.values());
            return  types.stream().map(Method::getValue).collect(Collectors.toList());
        }
    }

    public enum StepModel {
        //
        TEXT, STEP
    }
}
