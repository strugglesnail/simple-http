package com.struggle.http.handler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;

/**
 * Date类型处理器
 */
public class DatePropertyHandler extends AbstractPropertyHandler<Date> {

    private static String[] datePatterns = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss.SS", "yyyy-MM-dd HH:mm:ss.SSS"};

    @Override
    public boolean type(Class<?> propertyType) {
        return Date.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Date convertValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof String) {
                LocalDate localDate;
                String dateStr = (String) value;
                if (dateStr.contains(":") && dateStr.contains("-")) {
                    int length = dateStr.length();
                    String datePattern = Arrays.stream(datePatterns).filter(dp -> dp.length() == length).findFirst().orElse(null);
                    LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(datePattern));
                    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                } else  if (dateStr.contains("-")) {
                    localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } else if (dateStr.contains("/")){
                    localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                } else {
                    localDate = LocalDate.parse(dateStr);
                }
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            } else if (value instanceof Long) {
                Long timestamp = (Long) value;
                return Date.from(Instant.ofEpochMilli(timestamp));
            }
        } catch (DateTimeParseException e) {
            return null;
        }
        return null;
    }
}
