package com.struggle.http.handler;

/**
 * 整型类型处理器
 */
public class IntegerPropertyHandler extends AbstractPropertyHandler<Integer> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = int.class.isAssignableFrom(propertyType)) || Integer.class.isAssignableFrom(propertyType);
    }


    @Override
    protected Integer convertValue(Object value) {
        return Integer.valueOf(String.valueOf(value));
    }

    @Override
    protected Integer defaultConvertValue() {
        return isMatchBaseType ? (int) 0 : null;
    }
}
