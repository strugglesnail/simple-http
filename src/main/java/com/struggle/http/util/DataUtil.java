package com.struggle.http.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.struggle.http.annotation.MapIgnore;
import com.struggle.http.annotation.MapKey;
import com.struggle.http.handler.PropertyHandler;
import com.struggle.http.handler.PropertyHandlerRegistry;
import com.struggle.http.io.Resources;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataUtil {

    // list<map> 转换 list<T> 默认开启驼峰
    public static <T> List<T> listMapToConvertListBean(Class<T> clazz, List<Map<String, Object>> dataMap) {
        List<T> dataList = dataMap.stream().map(m -> mapToConvertBeanComposite(clazz, m, true)).collect(Collectors.toList());
        return dataList;
    }

    public static <T> List<T> listMapToConvertListBean(Class<T> clazz, List<Map<String, Object>> dataMap, boolean isToCamelCase) {
        List<T> dataList = dataMap.stream().map(m -> mapToConvertBeanComposite(clazz, m, isToCamelCase)).collect(Collectors.toList());
        return dataList;
    }

    public static <T> T mapToConvertBeanComposite(Class<T> clazz, Map<String, Object> dataMap, boolean isToCamelCase) {
        T data = isToCamelCase ? mapToConvertBeanForToCamelCase(clazz, dataMap) : mapToConvertBean(clazz, dataMap);
        return data;
    }

    // 匹配规则：字段名称和map的key完全匹配
    public static <T> T mapToConvertBean(Class<T> clazz, Map<String, Object> dataMap) {
        T data = Resources.newInstance(clazz);
        if (data == null) {
            return null;
        }

        mapToConvertBean(clazz, dataMap, data, false);
        return data;
    }

    // 匹配规则：字段名称和map的key()转成小写后匹配
    public static <T> T mapToConvertBeanForToCamelCase(Class<T> clazz, Map<String, Object> dataMap) {
        T data = Resources.newInstance(clazz);
        if (data == null) return null;
        // map是否key值需要转成驼峰类型
        Map<String, Object> newMap = new HashMap<>(dataMap.size());
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = StringUtils.toCamelCase(entry.getKey());
            newMap.put(key.toLowerCase(Locale.ENGLISH), entry.getValue());
        }
        dataMap = newMap;

        mapToConvertBean(clazz, dataMap, data, true);
        return data;
    }

    // map映射成bean
    private static <T> void mapToConvertBean(Class<T> clazz, Map<String, Object> dataMap, T data, boolean isToLowerCase) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            MapIgnore mapIgnore = field.getDeclaredAnnotation(MapIgnore.class);
            if (mapIgnore != null) {
                continue;
            }
            PropertyHandler typeHandler;
            Class<?> fieldType = field.getType();
            if ("java.util.List".equals(fieldType.getName())) {
                Class<?> genericType = getGenericType(field);
                typeHandler = PropertyHandlerRegistry.getTypeHandler(fieldType, genericType);
            } else {
                typeHandler = PropertyHandlerRegistry.getTypeHandler(fieldType);
            }
            if (typeHandler == null) {
                continue;
            }

            MapKey mapKey = field.getDeclaredAnnotation(MapKey.class);
            String propertyName = mapKey != null ? mapKey.value() : field.getName();
            if (isToLowerCase) {
                // 实体属性名称与map参数key都转换成小写
                propertyName = propertyName.toLowerCase(Locale.ENGLISH);
            }
            Object value = typeHandler.propertyValue(propertyName, dataMap);
            if (value == null) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, data, value);
        }
    }

    public static void main(String[] args) throws Exception {
//        List<String> list = new ArrayList<>();
//        Type type = list.getClass().getGenericSuperclass();
//        if (type instanceof ParameterizedType) {
//            ParameterizedType parameterizedType = (ParameterizedType) type;
//            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//            System.out.println(actualTypeArguments);
//        }
//        Field field = DtocPmsInspectionWorkDetail.class.getDeclaredField("workTool");
//        Field field1 = DtocPmsInspectionWorkDetail.class.getDeclaredField("localDate");
//        Field field2 = DtocPmsInspectionWorkDetail.class.getDeclaredField("localDateTime");
//        String signature = field.toGenericString();
//        System.out.println(signature);
//        getGenericTypeName(field);
        String t = "{ \"id\": \"43b9291521c6a90d0100a7855aa1e978\",\n" +
                "            \"cityOrgId\": \"ff8080814a616f0f014b34751db00492\",\n" +
                "            \"cityOrgName\": \"盐城\",\n" +
                "            \"maintOrgId\": \"ff8080814a616f0f014b347657950493\",\n" +
                "            \"maintOrgName\": \"东台市\",\n" +
                "            \"dkxBdzmc\": \"弶港变\",\n" +
                "            \"dkxXlmc\": \"10KV海堤线156,10kV弶开线153\",\n" +
                "            \"dkxId\": \"10DKX-137409,10DKX-160113\",\n" +
                "            \"gdsId\": null,\n" +
                "            \"gdsMc\": null,\n" +
                "            \"maintGroup\": \"弶港业务所\",\n" +
                "            \"outageStart\": \"2023-02-27 08:45:00\",\n" +
                "            \"outageEnd\": \"2023-02-27 13:00:00\",\n" +
                "            \"pushingDate\": \"2023-02-28 00:00:00\",\n" +
                "            \"tdsh\": 12,\n" +
                "            \"powoffid\": \"283701e7-083f-410e-94a0-b4dd026af413\",\n" +
                "            \"jxjhId\": \"283701e7-083f-410e-94a0-b4dd026af413\",\n" +
                "            \"tdhs\": 4,\n" +
                "            \"tdsc\": \"3.00\",\n" +
                "            \"tdxz\": \"计划\",\n" +
                "            \"checkStatus\": 5,\n" +
                "            \"sfdb\": true,\n" +
                "            \"dtOutageFragmentVos\": null}";

        JSONObject map = JSON.parseObject(t);
//        DtocPmisFreeze dtocPmsPdLinefz = mapToConvertBeanComposite(DtocPmisFreeze.class, map, false);
//        System.out.println(dtocPmsPdLinefz);
    }

    private static Class<?> getGenericType(Field field) {
        Type genericType = field.getGenericType();
//        System.out.println(genericType.getTypeName());
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(genericType.getTypeName());
        if (matcher.find()) {
            try {
                return Class.forName(matcher.group(1));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String mapString(Map<String, Object> x, String name) {
        Object o = x.get(name);
        if (o == null) return null;
        return (String) x.get(name);
    }

//    public static Date mapDate(Map<String, Object> x, String name) {
//        Object o = x.get(name);
//        if (o == null) return null;
//        return DateUtil.strToDate((String) x.get(name));
//    }

    public static Double mapDouble(Map<String, Object> x, String name) {
        Object o = x.get(name);
        if (o == null) return null;
        return (Double) o;
    }

    public static BigDecimal mapDecimal(Map<String, Object> x, String name) {
        Object o = x.get(name);
        if (o == null) return null;
        if (o instanceof String) {
            return new BigDecimal(String.valueOf(o));
        }
        return (BigDecimal) o;
    }


    public static Integer mapInt(Map<String, Object> x, String name) {
        Object o = x.get(name);
        if (o == null) return null;
        if (o instanceof String) {
            return Integer.valueOf((String) o);
        }
        return (Integer) o ;
    }

    public static Boolean mapBool(Map<String, Object> x, String name) {
        Object o = x.get(name);
        if (o == null) return null;
        return (Boolean) o ;
    }
}
