package com.struggle.http.handler;

import java.util.Map;

/**
 * 对象属性处理器
 */
public interface PropertyHandler {


    default boolean type(Class<?> propertyType) {return false;}

    // 入参对象属性类型及类型泛型类型：例如：List<Demo>, list是属性类型, Demo是泛型类型
    default boolean type(Class<?> propertyType, Class<?> genericType) {
        return false;
    }

    Object propertyValue(String propertyName, Map<String, Object> map);

}
