package com.struggle.http.spring;

import com.struggle.http.session.UrlSession;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Iterator;
import java.util.Set;

public class ClassPathUrlScanner extends ClassPathBeanDefinitionScanner {


    private UrlSession urlSession;


    private Class<? extends UrlFactoryBean> urlFactoryBeanClass = UrlFactoryBean.class;

//    private Class<? extends Annotation> annotationClass;


    public ClassPathUrlScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;
//        if (this.annotationClass != null) {
//            this.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
//            acceptAllInterfaces = false;
//        }
//
//        if (this.markerInterface != null) {
//            this.addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
//                protected boolean matchClassName(String className) {
//                    return false;
//                }
//            });
//            acceptAllInterfaces = false;
//        }

        if (acceptAllInterfaces) {
            this.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
                return true;
            });
        }

        this.addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
//            LOGGER.warn(() -> "No xml url was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for(Iterator var3 = beanDefinitions.iterator(); var3.hasNext();) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder)var3.next();
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
//            LOGGER.debug(() -> "Creating UrlFactoryBean with name '" + holder.getBeanName() + "';");
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(this.urlFactoryBeanClass);

            if (this.urlSession != null) {
                definition.getPropertyValues().add("urlSession", this.urlSession);
            }

            definition.setAutowireMode(2);
        }

    }

    // 指定接口类扫描
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
//            LOGGER.warn(() -> {
//                return "Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface. Bean already defined with the same name!";
//            });
            return false;
        }
    }


    public void setUrlSession(UrlSession urlSession) {
        this.urlSession = urlSession;
    }

    public void setUrlFactoryBeanClass(Class<? extends UrlFactoryBean> urlFactoryBeanClass) {
        this.urlFactoryBeanClass = urlFactoryBeanClass;
    }
}
