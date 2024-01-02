package com.struggle.http.handler;

import com.struggle.http.util.DataUtil;

import java.util.List;
import java.util.Map;

public class ListPropertyHandler<T> extends AbstractPropertyHandler<List<T>> {

    private Class<?> genericType;

    @Override
    public boolean type(Class<?> propertyType, Class<?> genericType) {
        this.genericType = genericType;
        return "java.util.List".equals(propertyType.getName());
    }

    @Override
    protected List<T> convertValue(Object value) {
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) value;
        List<T> listBean = (List<T>) DataUtil.listMapToConvertListBean(genericType, listMap, true);
        return listBean;
    }

}
