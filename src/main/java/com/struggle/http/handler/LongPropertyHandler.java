package com.struggle.http.handler;

/**
 * Long类型处理器
 */
public class LongPropertyHandler extends AbstractPropertyHandler<Long> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = long.class.isAssignableFrom(propertyType)) || Long.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Long convertValue(Object value) {
        return Long.valueOf(String.valueOf(value));
    }

    @Override
    protected Long defaultConvertValue() {
        return isMatchBaseType ? (long) 0 : null;
    }
}
