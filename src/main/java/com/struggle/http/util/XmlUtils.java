package com.struggle.http.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.dom4j.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xml解析工具类
 */
public class XmlUtils {

    public static JSONObject xmlToJson(String xml) {
        return elementToJson((strToDocument(xml)).getRootElement());
    }

    public static Map<String, Object> xmlToJsonReMap(String xml) {
        Document document = strToDocument(xml);
        if (document == null) {
            return new HashMap<>();
        }
        return elementToJsonReMap(document.getRootElement());
    }

    public static String jsonToXml(String json) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            JSONObject jObj = JSON.parseObject(json);
            jsonToXmlStr(jObj, buffer);
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String jsonToXmlForAttr(String json) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                    " <SOAP-ENV:Body>");
            JSONObject jObj = JSON.parseObject(json);
            jsonToXmlForAttr(jObj, buffer);
            buffer.append("</SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>");
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static Document strToDocument(String xml) {
        try {
            return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            return null;
        }
    }

    private static JSONObject elementToJson(Element node) {
        JSONObject result = new JSONObject();
        List<Attribute> listAttr = node.attributes();
        for (Attribute attr : listAttr) {
            result.put(attr.getName(), attr.getValue());
        }
        List<Element> listElement = node.elements();
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {
                if (e.attributes().isEmpty() && e.elements().isEmpty()) {
                    result.put(e.getName(), e.getTextTrim());
                } else {
                    if (!result.containsKey(e.getName())) {
                        result.put(e.getName(), new JSONArray());
                    }
                    ((JSONArray) result.get(e.getName())).add(elementToJson(e));
                }
            }
        }
        return result;
    }

    private static Map<String, Object> elementToJsonReMap(Element node) {
        Map<String, Object> result = new HashMap<>();
        List<Attribute> listAttr = node.attributes();
        for (Attribute attr : listAttr) {
            result.put(attr.getName(), attr.getValue());
        }
        List<Element> listElement = node.elements();
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {
                if (e.attributes().isEmpty() && e.elements().isEmpty()) {
                    result.put(e.getName(), e.getTextTrim());
                } else {
                    if (!result.containsKey(e.getName())) {
                        result.put(e.getName(), new ArrayList<>());
                    }
                    ((List) result.get(e.getName())).add(elementToJsonReMap(e));
                }
            }
        }
        return result;
    }

    private static void jsonToXmlStr(JSONObject jObj, StringBuffer buffer) {
        Set<Map.Entry<String, Object>> se = jObj.entrySet();
        for (Map.Entry<String, Object> en : se) {
            if ("com.alibaba.fastjson.JSONObject".equals(en.getValue().getClass().getName())) {
                buffer.append("<").append(en.getKey()).append(">");
                JSONObject jo = jObj.getJSONObject(en.getKey());
                jsonToXmlStr(jo, buffer);
                buffer.append("</").append(en.getKey()).append(">");
            } else if ("com.alibaba.fastjson.JSONArray".equals(en.getValue().getClass().getName())) {
                JSONArray jarray = jObj.getJSONArray(en.getKey());
                for (int i = 0; i < jarray.size(); i++) {
                    buffer.append("<").append(en.getKey()).append(">");
                    JSONObject jsonobject = jarray.getJSONObject(i);
                    jsonToXmlStr(jsonobject, buffer);
                    buffer.append("</").append(en.getKey()).append(">");
                }
            } else if ("java.lang.String".equals(en.getValue().getClass().getName())) {
                buffer.append("<").append(en.getKey()).append(">").append(en.getValue());
                buffer.append("</").append(en.getKey()).append(">");
            }
        }
    }

    private static void jsonToXmlForAttr(JSONObject jObj, StringBuffer buffer) {
        Set<Map.Entry<String, Object>> se = jObj.entrySet();
        for (Map.Entry<String, Object> en : se) {
            if ("com.alibaba.fastjson.JSONObject".equals(en.getValue().getClass().getName())) {
                buffer.append("<").append(en.getKey()).append(">");
                JSONObject jo = jObj.getJSONObject(en.getKey());
                jsonToXmlForAttr(jo, buffer);
                buffer.append("</").append(en.getKey()).append(">");
            } else if ("com.alibaba.fastjson.JSONArray".equals(en.getValue().getClass().getName())) {
                String key = en.getKey();
                if (key.contains("m:")) {
                    key = key + "  " + "xmlns:m=\"urn:sap-com:document:sap:soap:functions:mc-style\"";
                }
                buffer.append("<").append(key).append(">");

                JSONArray jarray = jObj.getJSONArray(en.getKey());
                for (int i = 0; i < jarray.size(); i++) {
                    JSONObject jsonobject = jarray.getJSONObject(i);
                    jsonToXmlForAttr(jsonobject, buffer);
                }
                buffer.append("</").append(en.getKey()).append(">");
            } else if ("java.lang.String".equals(en.getValue().getClass().getName())) {
                buffer.append("<").append(en.getKey()).append(">").append(en.getValue());
                buffer.append("</").append(en.getKey()).append(">");
            }
        }
    }

    public static List<Map> buildItemMap(String key, String value) {
        List<Map> lastLeve = new ArrayList<>(1);
        lastLeve.add(ImmutableMap.of(key, value));
        return Arrays.asList(ImmutableMap.of("item", lastLeve));
    }

    public static List<Map> buildItemMap(String key, String... values) {
        List<Map> list = Lists.newArrayList();
        for (String value : values) {
            Map itemMap = getItemMap(key, value);
            list.add(itemMap);
        }
        return list;
    }

    private static Map getItemMap(String key, String value) {
        List<Map> lastLeve = new ArrayList<>(1);
        lastLeve.add(ImmutableMap.of(key, value));
        Map<String, List<Map>> itemMap = ImmutableMap.of("item", lastLeve);
        return itemMap;
    }


    public static void main(String[] args) {
//        List<Map> list = buildItemMap("Bwart", "103", "105", "10221");
//        Map<String, Object> child = new HashMap<>(5);
//        child.put("TBwart", list);
//        System.out.println(child);
        Map<String, Object> childMap = fixedMap();
//        setDateMap(childMap);
        Map<String, Object> rootMap = getRootMap(childMap);
        String json = JSONUtil.toJsonPrettyStr(rootMap);
        String json1 = jsonToXmlForAttr(json);
        System.out.println(json1);


    }

    private static Map<String, Object> fixedMap() {
        Map<String, Object> child = new HashMap<>(1);
        child.put("TBwart", XmlUtils.buildItemMap("Bwart", "101", "105", "221", "222", "261", "262", "351"));
        return child;
    }

    private static void setDateMap(Map<String, Object> child) {
        child.put("BldatFrom", "2020");
        child.put("BldatTo", "2021");
    }

    private static Map<String, Object> getRootMap(Map<String, Object> childMap) {
        Map<String, Object> rootMap = ImmutableMap.of("m:ZwmsZhyyzxZckpzcx", Lists.newArrayList(childMap));
        return rootMap;
    }

    public static boolean isJsonObject(String jsonString) {
        // 匹配以花括号包围的JSON对象
        String regex = "^\\{.*\\}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonString);
        return matcher.matches();
    }

    public static boolean isJsonArray(String jsonString) {
        // 匹配以方括号包围的JSON数组
        String regex = "^\\[.*\\]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonString);
        return matcher.matches();
    }

    public static boolean isXmlRequest(Map<String, Object> headParam) {
        if (CollectionUtil.isNotEmpty(headParam) && headParam.containsKey("Content-Type") && ((String)headParam.get("Content-Type")).startsWith("text/xml")) {
            return true;
        }
        return false;
    }
}
