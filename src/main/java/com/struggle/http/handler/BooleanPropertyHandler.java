package com.struggle.http.handler;

/**
 * Boolean类型处理器
 */
public class BooleanPropertyHandler extends AbstractPropertyHandler<Boolean> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = boolean.class.isAssignableFrom(propertyType)) || Boolean.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Boolean convertValue(Object value) {
        return Boolean.valueOf(String.valueOf(value));
    }

    @Override
    protected Boolean defaultConvertValue() {
        return isMatchBaseType ? false : null;
    }
}
