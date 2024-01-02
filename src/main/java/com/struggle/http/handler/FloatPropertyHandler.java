package com.struggle.http.handler;

/**
 * 浮点型类型处理器
 */
public class FloatPropertyHandler extends AbstractPropertyHandler<Float> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = float.class.isAssignableFrom(propertyType)) || Float.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Float convertValue(Object value) {
        return Float.valueOf(String.valueOf(value));
    }

    @Override
    protected Float defaultConvertValue() {
        return isMatchBaseType ? (float) 0 : null;
    }
}
