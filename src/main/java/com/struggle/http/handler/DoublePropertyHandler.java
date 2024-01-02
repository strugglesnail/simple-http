package com.struggle.http.handler;

/**
 * Double类型处理器
 */
public class DoublePropertyHandler extends AbstractPropertyHandler<Double> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = double.class.isAssignableFrom(propertyType)) || Double.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Double convertValue(Object value) {
        return Double.valueOf(String.valueOf(value));
    }

    @Override
    protected Double defaultConvertValue() {
        return isMatchBaseType ? (double) 0 : null;
    }
}
