package com.struggle.http.handler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * LocalDateTime类型处理器
 */
public class LocalDateTimePropertyHandler extends AbstractPropertyHandler<LocalDateTime> {

    private static String[] datePatterns = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss.SS", "yyyy-MM-dd HH:mm:ss.SSS"};

    @Override
    public boolean type(Class<?> propertyType) {
        return LocalDateTime.class.isAssignableFrom(propertyType);
    }

    @Override
    protected LocalDateTime convertValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            String dateStr = (String) value;
            if (dateStr.contains(":") && dateStr.contains("-")) {
                int length = dateStr.length();
                String datePattern = Arrays.stream(datePatterns).filter(dp -> dp.length() == length).findFirst().orElse(null);
                LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(datePattern));
                return localDateTime;
            }
        } else if (value instanceof Long) {
            Long timestamp = (Long) value;
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        }
        return null;
    }
}
