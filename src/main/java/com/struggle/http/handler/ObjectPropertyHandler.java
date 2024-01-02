package com.struggle.http.handler;

/**
 * 引用类型处理器
 */
public class ObjectPropertyHandler extends AbstractPropertyHandler<Object> {

//    private Class<?> propertyType;

    @Override
    public boolean type(Class<?> propertyType) {
//        this.propertyType = propertyType;
        return "java.lang.Object".equals(propertyType.getName()) || "java.util.Map".equals(propertyType.getName());
    }

//    @Override
//    public Object propertyValue(String propertyName, Map map) {
//        Object obj = map;
////        if (StringUtils.isNotEmpty(json)) {
////            obj = JSONObject.parseObject(json, propertyType);
////        } else {
////            try {
////                obj = propertyType.newInstance();
////            } catch (InstantiationException e) {
////                e.printStackTrace();
////            } catch (IllegalAccessException e) {
////                e.printStackTrace();
////            }
////        }
//        return obj;
//    }

    @Override
    protected Object convertValue(Object value) {
        return value;
    }
}
