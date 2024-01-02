package com.struggle.http.handler;

/**
 * 字节类型处理器
 */
public class BytePropertyHandler extends AbstractPropertyHandler<Byte> {

    @Override
    public boolean type(Class<?> propertyType) {
        return (isMatchBaseType = byte.class.isAssignableFrom(propertyType)) || Byte.class.isAssignableFrom(propertyType);
    }

    @Override
    protected Byte convertValue(Object value) {
        return Byte.valueOf(String.valueOf(value));
    }

    @Override
    protected Byte defaultConvertValue() {
        return isMatchBaseType ? (byte) 0 : null;
    }
}
