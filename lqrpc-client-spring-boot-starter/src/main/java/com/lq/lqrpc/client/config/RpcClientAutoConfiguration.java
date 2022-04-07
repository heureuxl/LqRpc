package com.lq.lqrpc.client.config;


import com.lq.lqrpc.client.processor.RpcClientProcessor;
import com.lq.lqrpc.client.proxy.ClientStubProxyFactory;
import com.lq.lqrpc.core.balancer.DemoLoadBalance;
import com.lq.lqrpc.core.balancer.LoadBalance;
import com.lq.lqrpc.core.discover.DiscoveryService;
import com.lq.lqrpc.core.discover.ZookeeperDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * @ClassName: RpcClientAutoConfiguration
 * @Description: 服务消费者自动装配
 * @author: liuqi
 * @date: 2022/3/1 上午11:03
 * @Version: 0.1
 */
@Configuration
public class RpcClientAutoConfiguration {


    /**
     * 获取自动装装配属性对象
     */
    @Bean
    public RpcClientProperties rpcClientProperties(Environment environment){
        BindResult<RpcClientProperties> bind = Binder.get(environment).bind("lqrpc.client", RpcClientProperties.class);
        return bind.get();
    }

    /**
     * 获取代理工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientStubProxyFactory clientStubProxyFactory(){ return new ClientStubProxyFactory(); }

    /**
     * 按照装配负载均衡算法
     */
    @Primary
    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "lqrpc.client", name = "balance", havingValue = "DemoLoadBalance", matchIfMissing = true)
    public LoadBalance demoBalance(){return new DemoLoadBalance(); }

    /**
     * 初始化服务发现工具
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({RpcClientProperties.class, LoadBalance.class})
    public DiscoveryService discoveryService(@Autowired RpcClientProperties properties,
                                             @Autowired LoadBalance loadBalance){
        return new ZookeeperDiscoveryService(properties.getDiscoveryAddr(), loadBalance);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcClientProcessor rpcClientProcessor(@Autowired ClientStubProxyFactory clientStubProxyFactory,
                                                 @Autowired DiscoveryService discoveryService,
                                                 @Autowired RpcClientProperties properties) {
        return new RpcClientProcessor(properties, discoveryService, clientStubProxyFactory);
    }


}
