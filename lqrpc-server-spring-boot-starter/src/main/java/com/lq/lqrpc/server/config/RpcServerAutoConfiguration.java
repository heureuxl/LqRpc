package com.lq.lqrpc.server.config;

import com.lq.lqrpc.core.register.RegistryService;
import com.lq.lqrpc.core.register.ZookeeperRegistryService;
import com.lq.lqrpc.server.RpcServerProvider;
import com.lq.lqrpc.server.transport.NettyRpcServer;
import com.lq.lqrpc.server.transport.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RpcServerAutoConfiguration
 * @Description: 服务提供者自动装配
 * @author: liuqi
 * @date: 2022/3/3 上午10:19
 * @Version: 0.1
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {

    @Autowired
    private RpcServerProperties properties;

    // 加载连接注册中心
    @Bean
    @ConditionalOnMissingBean
    public RegistryService registryService(){
        return new ZookeeperRegistryService(properties.getRegisterAddress());
    }

    // 开启netty，处理请求
    @Bean
    @ConditionalOnMissingBean(RpcServer.class)
    public RpcServer rpcServer(){
        return new NettyRpcServer();
    }

    // 实例化bean后进行服务注册，并且当服务销毁后同步删除注册中心中服务
    @Bean
    @ConditionalOnMissingBean(RpcServerProvider.class)
    public RpcServerProvider rpcServerProvider(){
        return rpcServerProvider();
    }
}
