package com.struggle.http.parse;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;

import java.util.Map;
import java.util.Properties;

public class PropertyParser {


    private PropertyParser() {
    }

    public static String parse(String string, Map<String, Object> map) {
        Properties variables = new Properties();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            variables.setProperty(entry.getKey(), String.valueOf(entry.getValue()));
        }
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    public static String parse(String string, Properties variables) {
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    private static class VariableTokenHandler implements TokenHandler {
        private final Properties variables;
        private final boolean enableDefaultValue;
        private final String defaultValueSeparator;

        private VariableTokenHandler(Properties variables) {
            this.variables = variables;
            this.enableDefaultValue = Boolean.parseBoolean(this.getPropertyValue("org.apache.ibatis.parsing.PropertyParser.enable-default-value", "false"));
            this.defaultValueSeparator = this.getPropertyValue("org.apache.ibatis.parsing.PropertyParser.default-value-separator", ":");
        }

        private String getPropertyValue(String key, String defaultValue) {
            return this.variables == null ? defaultValue : this.variables.getProperty(key, defaultValue);
        }

        @Override
        public String handleToken(String content) {
            if (this.variables != null) {
                String key = content;
                if (this.enableDefaultValue) {
                    int separatorIndex = content.indexOf(this.defaultValueSeparator);
                    String defaultValue = null;
                    if (separatorIndex >= 0) {
                        key = content.substring(0, separatorIndex);
                        defaultValue = content.substring(separatorIndex + this.defaultValueSeparator.length());
                    }

                    if (defaultValue != null) {
                        return this.variables.getProperty(key, defaultValue);
                    }
                }

                if (this.variables.containsKey(key)) {
                    return this.variables.getProperty(key);
                }
            }

            return "${" + content + "}";
        }
    }
}
