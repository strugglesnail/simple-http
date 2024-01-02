package com.struggle.http.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({UrlScannerRegistrar.class})
public @interface UrlScan {

    String[] value() default {};

//    String[] basePackages() default {};

//    Class<? extends UrlFactoryBean> factoryBean() default UrlFactoryBean.class;
}
