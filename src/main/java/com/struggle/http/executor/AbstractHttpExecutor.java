package com.struggle.http.executor;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.struggle.http.ognl.OgnlUtil;
import com.struggle.http.exception.EmptyBodyUrlException;
import com.struggle.http.exception.ResponseBodyFormatUrlException;
import com.struggle.http.exception.UrlException;
import com.struggle.http.io.Resources;
import com.struggle.http.parse.UrlDefinition;
import com.struggle.http.util.DataUtil;
import com.struggle.http.util.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * http请求执行器模板
 */
public abstract class AbstractHttpExecutor implements HttpExecutor {

    private static Logger logger = LoggerFactory.getLogger(AbstractHttpExecutor.class);


    @Override
    public Object execute(UrlDefinition definition) {
        Object result;
        try {
            Map<String, Object> heads = definition.getHeads();
            String responseBody = httpResponse(definition);

            // 处理响应结果
            if (StringUtils.isNotBlank(responseBody)) {
                // 响应结果是对象则映射成map
                if (XmlUtils.isJsonObject(responseBody)) {
                    Map<String, Object> resultMap = JSON.parseObject(responseBody, Map.class);
                    result = resultMap;
                } else if (XmlUtils.isJsonArray(responseBody)) { // 响应结果是列表
                    result = JSON.parseObject(responseBody, List.class);
                } else  if (XmlUtils.isXmlRequest(heads)) { // 响应结果是XML
                    result = XmlUtils.xmlToJsonReMap(responseBody);
                } else {
                    throw new ResponseBodyFormatUrlException("Http response body format error！" + responseBody);
                }
                if (result instanceof Map) {
                    List<String> responseData = definition.getResponseBody();
                    if (CollectionUtil.isNotEmpty(responseData)) {
                        String bodyResult = responseData.get(0);
                        Object newResult = OgnlUtil.getValue(bodyResult, result);
                        if (newResult == null) {
                            logger.error("Unexpected result data: {}", responseBody);
                            return null;
                        }
                        result = newResult;
                    }
                }

                // 映射指定的实体
                String resultType = definition.getResultType();
                if (result != null && StringUtils.isNotBlank(resultType)) {
                    Class<?> resultClass = Resources.classForName(resultType);
                    if (result instanceof Map) {
                        result = DataUtil.mapToConvertBean(resultClass, (Map<String, Object>) result);
                    } else if (result instanceof List) {
                        result = DataUtil.listMapToConvertListBean(resultClass, (List<Map<String, Object>>) result);
                    }
                }
            } else {
                // 响应结果为空
                throw new EmptyBodyUrlException("Empty response data！");
            }
        } catch (Exception e) {
            if (e instanceof EmptyBodyUrlException || e instanceof ResponseBodyFormatUrlException || e instanceof ClassNotFoundException) {
                throw new UrlException(e);
            }
            int retry = Integer.valueOf(definition.getRetry());
            for (int i = 0; i < retry; i++) {
                String newRetry = String.valueOf(retry > 0 ? retry - 1 : 0);
                definition.setRetry(newRetry);
                System.out.println("newRetry: " + newRetry);
                result = execute(definition);
                if (result != null) {
                    return result;
                }
            }
            throw new UrlException(e);
        }
        return result;
    }

    // 获取响应结果
    protected abstract String httpResponse(UrlDefinition definition);
}
