package com.struggle.http.ognl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.ibatis.ognl.NoSuchPropertyException;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OgnlUtil {

    private final static OgnlContext context = new OgnlContext(null, null, new DefaultMemberAccess(true));

    /**
     *
     * @param expression 设置对象属性表达式
     * @param obj 实例对象
     * @return
     */
    public static void setValue(String expression, Object obj) {
        try {
            Ognl.setValue(expression, context, obj);
        } catch (OgnlException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param expression 设置对象属性表达式
     * @param root  实例对象
     * @param value 值
     * @return
     */
    public static void setValue(String expression, Object root, Object value) {
        try {
            Ognl.setValue(expression, context, root, value);
        } catch (OgnlException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param expression 获取对象属性表达式
     * @param obj 实例对象
     * @return 对象属性值
     */
    public static Object getValue(String expression, Object obj) {
        Object value = null;
        try {
            value = Ognl.getValue(expression, context, obj);
        } catch (OgnlException e) {
            if (!(e instanceof NoSuchPropertyException)) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     *  用于构造对象
     * @param expression 获取对象属性表达式
     * @return 对象属性值
     */
    public static Object getValue(String expression) {
        Object value = getValue(expression, null);
        return value;
    }

    public static void main(String[] args) {
        Map<String, Map<String, Object>> config = new HashMap<>();
        Map<String, Object> oms = new HashMap<>();
        oms.put("ip", "1111.1111");
        config.put("oms", oms);
//        String value = (String) OgnlUtil.getValue("oms.ip.x", config);
//        System.out.println(value);

//        String parse = PropertyParser.parse("http://xxxx", oms);
//        System.out.println(parse);
        // 构造Map
        Object value = OgnlUtil.getValue("#{'fool':'fff'}");
        // 使用指定map构造
        Object value1 = OgnlUtil.getValue("#@java.util.HashMap@{'fool':'fff'}");
        System.out.println(value);
        System.out.println(value1);

        Map<String, Object> param = new HashMap<>();
        param.put("page", 1);
        param.put("perpage", 1000);
        param.put("moduleId", 11111);
        ImmutableMap<String, String> map1 = ImmutableMap.of("compare", "=", "fieldName", "baseParam.get(0)", "fieldValue", "baseParam.get(1)");
        List<Map> list = new ArrayList<>();
        list.add(map1);
        param.put("filters", list);
//        OgnlUtil.setValue("param['filters'].add('222')", param, param);
//        System.out.println(param);
//        setValue("filters.add()", param, ImmutableMap.of());
//        filters.add("111");
        System.out.println(param);

//        ImmutableList<String> of = ImmutableList.of("1", "@");
//        of.add("2");
        ArrayList<String> newArrayList = Lists.newArrayList("1", "2");
        newArrayList.add("3");
        System.out.println(newArrayList);

    }
}
