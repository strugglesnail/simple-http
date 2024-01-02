package com.struggle.http.spring;


import com.struggle.http.parse.UrlRegistry;
import com.struggle.http.parse.SimpleUrlDefinitionRegistry;
import com.struggle.http.parse.UrlDefinitionRegistry;
import com.struggle.http.parse.UrlMapperRegistry;
import com.struggle.http.parse.XmlUrlDefinitionReader;
import com.struggle.http.session.DefaultUrlSession;
import com.struggle.http.session.UrlSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 同步url配置
 */
@Configuration
@EnableConfigurationProperties({SyncProperties.class})
public class SyncAutoConfiguration implements InitializingBean {

    @Autowired
    private SyncProperties properties;

    private UrlRegistry registry;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化xml注册信息
        UrlDefinitionRegistry definitionRegistry = new SimpleUrlDefinitionRegistry();
        UrlMapperRegistry mapperRegistry = new UrlMapperRegistry();
        registry = new UrlRegistry(definitionRegistry, mapperRegistry);

        XmlUrlDefinitionReader reader = new XmlUrlDefinitionReader(registry, properties.transformMapKeys());
        reader.loadUrlDefinitions(properties.getUrlLocations());
    }

    @Bean
    public UrlSession urlSession() {
        DefaultUrlSession session = new DefaultUrlSession(registry);
        return session;
    }

    // 此注册代码被UrlScan注解代替
//    @Configuration
//    @Import({AutoConfiguredUrlScannerRegistrar.class})
//    @ConditionalOnMissingBean({UrlFactoryBean.class, UrlScannerConfigurer.class})
//    public static class MapperScannerRegistrarNotFoundConfiguration {
//    }
//
//    public static class AutoConfiguredUrlScannerRegistrar implements ImportBeanDefinitionRegistrar {
//
//        @Override
//        public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
//            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UrlScannerConfigurer.class);
////            builder.addPropertyValue("processPropertyPlaceHolders", true);
////            builder.addPropertyValue("annotationClass", Mapper.class);
//            builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(Arrays.asList("com.struggle.http.mapper")));
////            BeanWrapper beanWrapper = new BeanWrapperImpl(MapperScannerConfigurer.class);
//            registry.registerBeanDefinition(UrlScannerConfigurer.class.getName(), builder.getBeanDefinition());
//        }
//    }
}
