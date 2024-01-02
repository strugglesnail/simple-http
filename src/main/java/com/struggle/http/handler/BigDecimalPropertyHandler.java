package com.struggle.http.handler;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * BigDecimal类型处理器
 */
public class BigDecimalPropertyHandler extends AbstractPropertyHandler<BigDecimal> {

    @Override
    public boolean type(Class<?> parameterType) {
        return BigDecimal.class.isAssignableFrom(parameterType);
    }

    @Override
    protected BigDecimal convertValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            String newStr = (String) value;
            if (StringUtils.isBlank(newStr)) {
                return null;
            }
            return new BigDecimal(newStr);
        } else if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        } else if (value instanceof Long) {
            return new BigDecimal((Long) value);
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return new BigDecimal((Double) value);
        } else if (value instanceof Float) {
            return new BigDecimal(String.valueOf((Float) value));
        }
        return null;
    }
}
