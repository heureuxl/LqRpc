package com.lq.lqrpc.core.processor;

import com.lq.lqrpc.core.annotation.LqRpcAutowired;
import com.lq.lqrpc.core.config.RpcClientProperties;
import com.lq.lqrpc.core.discover.DiscoveryService;
import com.lq.lqrpc.core.proxy.ClientStubProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @ClassName: RpcClientProcessor
 * @Description: 后置处理器 将所有bean全部动态修改为代理对象
 * @author: liuqi
 * @date: 2022/3/3 下午5:36
 * @Version: 0.0.1
 */
public class RpcClientProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private RpcClientProperties properties;
    private DiscoveryService discoveryService;
    private ClientStubProxyFactory clientStubProxyFactory;

    public RpcClientProcessor(RpcClientProperties properties, DiscoveryService discoveryService, ClientStubProxyFactory clientStubProxyFactory) {
        this.properties = properties;
        this.discoveryService = discoveryService;
        this.clientStubProxyFactory = clientStubProxyFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        for (String beanDefinitionName : configurableListableBeanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null){
                Class<?> aClass = ClassUtils.resolveClassName(beanClassName, this.getClass().getClassLoader());
                ReflectionUtils.doWithFields(aClass, field -> {
                    LqRpcAutowired annotation = AnnotationUtils.getAnnotation(field, LqRpcAutowired.class);
                    if (annotation != null){
                        Object bean = applicationContext.getBean(aClass);
                        field.setAccessible(true);
                        ReflectionUtils.setField(field ,bean ,clientStubProxyFactory.getProxy(field.getType(), annotation.version(), discoveryService, properties));
                    }
                });
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
