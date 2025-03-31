package com.template.spring.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;


public class LoggingUtil {

    public static String maskField(Object obj) {
        if (obj == null) {
            return "null";
        }

        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder result = new StringBuilder(clazz.getSimpleName()).append("{");

            for (Field field : fields) {
                // Skip static fields to prevent InaccessibleObjectException
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);
                Object value = field.get(obj);

                if (field.isAnnotationPresent(Sensitive.class)) {
                    result.append(field.getName()).append("=****, ");
                } else {
                    result.append(field.getName()).append("=")
                            .append(formatValue(value)).append(", ");
                }
            }

            if (result.length() > clazz.getSimpleName().length() + 1) {
                result.setLength(result.length() - 2);
            }
            result.append("}");
            return result.toString();
        } catch (IllegalAccessException e) {
            return "Error masking sensitive data";
        }
    }

    public static String maskSensitiveData(Object result) {
        if (result == null) {
            return "null";
        } else if (result instanceof Optional<?> optionalResult) {
            return optionalResult.map(LoggingUtil::maskSensitiveData).orElse("Optional.empty");
        } else if (result instanceof Collection<?> collection) {
            return maskCollection(collection);
        } else if (result instanceof Map<?, ?> map) {
            return maskMap(map);
        } else {
            return maskField(result);
        }
    }

    private static String maskCollection(Collection<?> collection) {
        StringBuilder maskedCollection = new StringBuilder("[");
        for (Object item : collection) {
            maskedCollection.append(maskSensitiveData(item)).append(", ");
        }
        if (!collection.isEmpty()) {
            maskedCollection.setLength(maskedCollection.length() - 2);
        }
        maskedCollection.append("]");
        return maskedCollection.toString();
    }

    private static String maskMap(Map<?, ?> map) {
        StringBuilder maskedMap = new StringBuilder("{");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            maskedMap.append(entry.getKey()).append("=")
                    .append(maskSensitiveData(entry.getValue())).append(", ");
        }
        if (!map.isEmpty()) {
            maskedMap.setLength(maskedMap.length() - 2);
        }
        maskedMap.append("}");
        return maskedMap.toString();
    }

    private static String formatValue(Object value) {
        if (value instanceof Collection<?> || value instanceof Map<?, ?>) {
            return maskSensitiveData(value);
        } else {
            return String.valueOf(value);
        }
    }
}