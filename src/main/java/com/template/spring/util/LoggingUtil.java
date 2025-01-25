package com.template.spring.util;

import java.lang.reflect.Field;
import java.util.Optional;

public class LoggingUtil {

    public static String maskField(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder result = new StringBuilder(clazz.getSimpleName() + "{");

            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (field.isAnnotationPresent(Sensitive.class)) {
                    result.append(field.getName()).append("=****, ");
                } else {
                    result.append(field.getName()).append("=").append(value).append(", ");
                }
            }

            if (result.length() > 2) {
                result.setLength(result.length() - 2);
            }
            result.append("}");
            return result.toString();
        } catch (IllegalAccessException e) {
            return "Error masking sensitive data";
        }
    }

    public static String maskSensitiveData(Object result) {
        if (result instanceof Optional) {
            Optional<?> optionalResult = (Optional<?>) result;
            return optionalResult.map(LoggingUtil::maskSensitiveData)
                    .orElse("Optional.empty");
        } else {
            return maskField(result);
        }
    }
}