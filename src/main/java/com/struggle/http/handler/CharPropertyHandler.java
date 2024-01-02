package com.struggle.http.handler;

/**
 * char类型处理器
 */
public class CharPropertyHandler extends AbstractPropertyHandler<Character> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = propertyType.isAssignableFrom(char.class)) || Character.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Character convertValue(Object value) {
        return Character.valueOf((Character) value);
    }

    @Override
    protected Character defaultConvertValue() {
        return isMatchBaseType ? (char) 0 : null;
    }
}
