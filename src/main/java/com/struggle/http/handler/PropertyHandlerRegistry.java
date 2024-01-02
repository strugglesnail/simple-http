package com.struggle.http.handler;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对象属性类型注册器
 */
public class PropertyHandlerRegistry {

    private static final List<PropertyHandler> typeHandlerList = new ArrayList<>(16);

    static {
        register(new BooleanPropertyHandler());

        register(new BytePropertyHandler());

        register(new ShortPropertyHandler());

        register(new IntegerPropertyHandler());

        register(new LongPropertyHandler());

        register(new FloatPropertyHandler());

        register(new DoublePropertyHandler());

        register(new CharPropertyHandler());
        register(new StringPropertyHandler());

        register(new BigDecimalPropertyHandler());

        register(new DatePropertyHandler());

        register(new ObjectPropertyHandler());

        register(new ListPropertyHandler());

        register(new LocalDatePropertyHandler());

        register(new LocalDateTimePropertyHandler());
    }

    private static void register(PropertyHandler handler) {
        typeHandlerList.add(handler);
    }

    public static PropertyHandler getTypeHandler(Class propertyType) {
        PropertyHandler objectPropertyHandler = new EntityPropertyHandler();
        PropertyHandler typeHandler = typeHandlerList.stream().filter(type -> type.type(propertyType)).findFirst().orElse(objectPropertyHandler);
        return typeHandler;
    }

    public static PropertyHandler getTypeHandler(Class propertyType, Class genericType) {
        PropertyHandler objectPropertyHandler = new EntityPropertyHandler();
        PropertyHandler typeHandler = typeHandlerList.stream().filter(type -> type.type(propertyType, genericType)).findFirst().orElse(objectPropertyHandler);
        return typeHandler;
    }
}
