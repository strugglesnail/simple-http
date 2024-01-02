package com.struggle.http.parse;

import com.struggle.http.exception.NoSuchUrlAttributeException;
import com.struggle.http.exception.UrlDefinitionParsingException;
import com.struggle.http.executor.HttpExecutorFactory;
import com.struggle.http.executor.HttpExecutor;
import com.struggle.http.io.Resources;
import com.struggle.http.proxy.UrlsProxyFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class DefaultUrlDefinitionDocumentReader implements UrlDefinitionDocumentReader {

    private final Log logger = LogFactory.getLog(this.getClass());

    private UrlRegistry register;

    private Map<String, Object> variables;

    @Override
    public void registerUrlDefinitions(Document document, UrlRegistry register) throws Exception {
        this.register = register;
        doRegisterUrlDefinitions(document.getDocumentElement());
    }

    protected void doRegisterUrlDefinitions(Element root) throws Exception {
        UrlDefinitionRegistry definitionRegistry = register.getDefinitionRegistry();
        UrlMapperRegistry mapperRegistry = register.getMapperRegistry();
        // 创建UrlDefinitionHolderList
        UrlDefinitionHolder holder = new UrlDefinitionHolder();
        Map<String, UrlDefinition> urlDefinitionMap = new HashMap<>();
        holder.setUrlDefinitionMap(urlDefinitionMap);

        if (!root.hasAttribute("namespace") || !root.hasAttribute("type")) {
            throw new NoSuchUrlAttributeException("No such namespace or type attribute for urls element！");
        }

        // URL对应的接口
        String namespace = root.getAttribute("namespace").trim();
        holder.setNamespace(namespace);
        definitionRegistry.registerUrlDefinitionByNamespace(namespace, holder);

        // URL对应的模块
        String type = root.getAttribute("type").trim();
        holder.setType(type);
        definitionRegistry.registerUrlDefinitionByType(type, holder);

        // 遍历URL标签
        NodeList nl = root.getChildNodes();
        for(int i = 0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                parseDefaultElement(ele, urlDefinitionMap);
            }
        }

        // 代理逻辑
        Class<?> mapperClass = Resources.classForName(namespace);
        holder.setMapperClass(mapperClass);
        UrlsProxyFactory proxyFactory = new UrlsProxyFactory(holder);
        mapperRegistry.registerUrlMapper(mapperClass, proxyFactory);
    }

    // 解析url元素
    private void parseDefaultElement(Element ele, Map<String, UrlDefinition> urlDefinitionMap) {
        if (this.nodeNameEquals(ele, "url")) {
            // 创建UrlDefinition
            UrlDefinition urlDefinition = new GenericUrlDefinition();
            // 解析Url元素属性
            parseUrlDefinitionAttribute(ele, urlDefinition);
            // 解析Url元素子元素
            parseUrlDefinitionElement(ele, urlDefinition);
            // 注册url信息
            urlDefinitionMap.put(urlDefinition.getId(), urlDefinition);
            // 打印Url对象
            logger.info(urlDefinition);
        }

    }

    // 解析Url元素属性
    private void parseUrlDefinitionAttribute(Element ele, UrlDefinition urlDefinition) {
        // URL唯一标识
        if (ele.hasAttribute("id")) {
            String id = ele.getAttribute("id").trim();
            urlDefinition.setId(id);
        }

        // URL请求方式
        if (ele.hasAttribute("request-method")) {
            String requestMethod = ele.getAttribute("request-method");
            urlDefinition.setRequestMethod(requestMethod);
        }

        // URL响应类型
        if (ele.hasAttribute("result-type")) {
            String resultType = ele.getAttribute("result-type");
            urlDefinition.setResultType(resultType);
        }

        // URL请求重试次数
        if (ele.hasAttribute("retry")) {
            String retry = ele.getAttribute("retry");
            urlDefinition.setRetry(retry);
        }

        // URL请求执行器
        String executor = ele.getAttribute("executor");
        HttpExecutor httpExecutor;
        // 设置默认指定的执行器
        if (StringUtils.isBlank(executor)) {
            String requestMethod = urlDefinition.getRequestMethod();
            if (StringUtils.isBlank(requestMethod)) {
                throw new UrlDefinitionParsingException("url element must contain one `request-method` attribute.");
            }
            httpExecutor = HttpExecutorFactory.newExecutor(requestMethod);
        } else {
            // 实现自定义的执行器
            try {
                httpExecutor = Resources.newInstance(executor);
            } catch (ClassNotFoundException e) {
                throw new UrlDefinitionParsingException(executor + " is not illegal `executor` attribute.", e);
            }
        }
        if (httpExecutor == null) {
            throw new UrlDefinitionParsingException("Not found a suitable executor, please implements a custom executor.");
        }
        urlDefinition.setExecutor(httpExecutor);

    }

    // 解析Url元素子元素
    public void parseUrlDefinitionElement(Element ele, UrlDefinition urlDefinition) {
        // URL请求参数、URL请求头参数、URL依赖
        Map<String, Object> propMap = new HashMap<>();
        Map<String, Object> headMap = new HashMap<>();
        List<String> responseBodies = new ArrayList<>();
        NodeList nl = ele.getChildNodes();
        for(int i = 0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            // URL请求地址
            if (nodeNameEquals(node, "request-url")) {
                Element element = (Element)node;
                String requestUrlText = element.getTextContent();
                requestUrlText = parseVariables(requestUrlText);
                urlDefinition.setRequestUrl(requestUrlText);
            } else if (nodeNameEquals(node, "description")) { // URL描述
                Element element = (Element)node;
                String description = element.getTextContent();
                urlDefinition.setDescription(description);
            } else if (nodeNameEquals(node, "params")) {
                NodeList paramsChildNodes = node.getChildNodes();
                for (int j = 0; j < paramsChildNodes.getLength(); j++) {
                    Node propNode = paramsChildNodes.item(j);
                    if (nodeNameEquals(propNode, "property")) {
                        Element propElement = (Element)propNode;
                        parsePropertyElement(propElement, propMap);
//                        String key = propElement.getAttribute("key");
//                        String value = propElement.getAttribute("value");
//                        value = parseVariables(value);
//                        propMap.put(key, value);
                    }
                }
                urlDefinition.setParams(propMap);
            } else if (nodeNameEquals(node, "heads")) {
                NodeList headsChildNodes = node.getChildNodes();
                for (int j = 0; j < headsChildNodes.getLength(); j++) {
                    Node headNode = headsChildNodes.item(j);
                    if (nodeNameEquals(headNode, "head")) {
                        Element metaElement = (Element)headNode;
                        String key = metaElement.getAttribute("key");
                        String value = metaElement.getAttribute("value");
                        value = parseVariables(value);
                        headMap.put(key, value);
                    }
                }
                urlDefinition.setHeads(headMap);
            } else if (nodeNameEquals(node, "response-body")) {
                NodeList responseBodyChildNodes = node.getChildNodes();
                for (int j = 0; j < responseBodyChildNodes.getLength(); j++) {
                    Node bodyNode = responseBodyChildNodes.item(j);
                    if (nodeNameEquals(bodyNode, "body")) {
                        Element element = (Element) bodyNode;
                        String bodyContent = element.getTextContent();
                        responseBodies.add(bodyContent);
                    }
                }
                urlDefinition.setResponseBody(responseBodies);
            }
        }
    }

    // 解析property元素及属性
    private void parsePropertyElement(Element propElement, Map<String, Object> propMap) {
        String elementKey = propElement.getAttribute("key");
        NodeList childNodes = propElement.getChildNodes();
        Element subElement = null;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node subNode = childNodes.item(i);
            if (subNode instanceof Element) {
                if (subElement != null) {
                    throw new UrlDefinitionParsingException(elementKey + " must not contain more than one sub-element.");
                } else {
                    subElement = (Element)subNode;
                }
            }
        }

        boolean hasValueAttribute = propElement.hasAttribute("value");
        // 如果property元素既有属性value又有子元素, 则抛出异常
        if (hasValueAttribute && subElement != null) {
            throw new UrlDefinitionParsingException(elementKey + " is only allowed to contain either 'value' attribute OR sub-element.");
        }  else if (hasValueAttribute) {
            // 只有value属性的情况下只解析value值
            String value = propElement.getAttribute("value");
            value = parseVariables(value);
            propMap.put(elementKey, value);
        } else if (subElement != null) {
            this.parsePropertySubElement(subElement, elementKey, propMap);
        }
    }

    // 解析property元素中的子元素, 集合元素例如map, list
    private void parsePropertySubElement(Element subElement, String elementKey, Map<String, Object> propMap) {
        NodeList nodeList = subElement.getChildNodes();
        if (nodeNameEquals(subElement, "map")) {
            Map<String, Object> subMap = parseMapElement(nodeList);
            propMap.put(elementKey, subMap);
        } else if (nodeNameEquals(subElement, "list")) {
            System.out.println("");
            System.out.println("--------------");
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node listNode = nodeList.item(i);
                if (!(listNode instanceof Element)) {
                    continue;
                }
                Map<String, Object> listMap = parseMapElement(listNode.getChildNodes());
                mapList.add(listMap);
            }
            propMap.put(elementKey, mapList);
        }
    }

    // 解析map元素并转换成map集合
    private Map<String, Object> parseMapElement(NodeList nodeList) {
        Map<String, Object> subMap = new LinkedHashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node entryNode = nodeList.item(i);
            if (!(entryNode instanceof Element)) {
                continue;
            }
            Element entryElement = (Element) entryNode;
            String key = entryElement.getAttribute("key");
            String value = entryElement.getAttribute("value");
            value = parseVariables(value);
            subMap.put(key, value);
            System.out.println(key + ", " + value);
        }
        return subMap;
    }

    private String parseVariables(String text) {
        if (variables != null) {
            text = PropertyParser.parse(text, variables);
        }
        return text;
    }


    public boolean nodeNameEquals(Node node, String desiredName) {
        return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
    }


    @Override
    public void urlVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
