package com.struggle.http.handler;

import org.apache.commons.lang3.StringUtils;

/**
 * String类型处理器
 */
public class StringPropertyHandler extends AbstractPropertyHandler<String> {

    @Override
    public boolean type(Class<?> parameterType) {
        return String.class.isAssignableFrom(parameterType);
    }

    @Override
    protected String convertValue(Object value) {
        String str = String.valueOf(value);
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return str;
    }
}
