package com.struggle.http.handler;

import com.struggle.http.util.DataUtil;

import java.util.Map;

/**
 * 自定义实体对象类型处理器
 */
public class EntityPropertyHandler<T> extends AbstractPropertyHandler<T> {


    private Class<?> propertyType;

    @Override
    public boolean type(Class<?> propertyType) {
        this.propertyType = propertyType;
        return true;
    }

    @Override
    protected T convertValue(Object value) {
        T o = (T) DataUtil.mapToConvertBean(propertyType, (Map<String, Object>) value);
        return o;
    }
}
