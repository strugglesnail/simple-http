package com.struggle.http.handler;

import java.util.Map;

public abstract class AbstractPropertyHandler<T> implements PropertyHandler {

    protected boolean isMatchBaseType = false;

//    @Override
//    public Object parameterValue(String parameterName, Map map, String json) {
//        return parameterValue(parameterName, map);
//    }

    @Override
    public Object propertyValue(String propertyName, Map map) {
        // 是否存在该参数值
        if (map.containsKey(propertyName)) {
            // 赋值参数
            Object o = map.get(propertyName);
            if (o != null) {
                return convertValue(o);
            }
        }
        return defaultConvertValue();
    }

    protected T defaultConvertValue() {
        return null;
    }

    protected abstract T convertValue(Object value);
}
