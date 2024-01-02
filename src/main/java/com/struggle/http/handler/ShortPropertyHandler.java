package com.struggle.http.handler;

/**
 * short类型处理器
 */
public class ShortPropertyHandler extends AbstractPropertyHandler<Short> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = short.class.isAssignableFrom(propertyType)) || Short.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Short convertValue(Object value) {
        return Short.valueOf(String.valueOf(value));
    }

    @Override
    protected Short defaultConvertValue() {
        return isMatchBaseType ? (short) 0 : null;
    }
}
