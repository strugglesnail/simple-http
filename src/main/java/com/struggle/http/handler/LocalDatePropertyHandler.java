package com.struggle.http.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate类型处理器
 */
public class LocalDatePropertyHandler extends AbstractPropertyHandler<LocalDate> {


    @Override
    public boolean type(Class<?> propertyType) {
        return LocalDate.class.isAssignableFrom(propertyType);
    }

    @Override
    protected LocalDate convertValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            LocalDate localDate;
            String dateStr = (String) value;
            if (dateStr.contains("-")) {
                localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (dateStr.contains("/")){
                localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            } else {
                localDate = LocalDate.parse(dateStr);
            }
            return localDate;
        } else if (value instanceof Long) {
            Long timestamp = (Long) value;
            return LocalDate.ofEpochDay(timestamp);
        }
        return null;
    }
}
