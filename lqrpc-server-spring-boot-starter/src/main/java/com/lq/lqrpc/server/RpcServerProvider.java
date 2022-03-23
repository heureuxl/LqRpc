package com.lq.lqrpc.server;

import com.lq.lqrpc.core.common.ServiceInfo;
import com.lq.lqrpc.core.common.ServiceNameUtil;
import com.lq.lqrpc.core.register.RegistryService;
import com.lq.lqrpc.server.annotation.LqRpcService;
import com.lq.lqrpc.server.config.RpcServerProperties;
import com.lq.lqrpc.server.store.LocalServerCache;
import com.lq.lqrpc.server.transport.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @ClassName: RpcServerProvider
 * @Description: 实例化bean后进行服务注册，并且当服务销毁后同步删除注册中心中服务
 * @author: liuqi
 * @date: 2022/3/9 下午5:26
 * @Version: 0.0.1
 */
@Slf4j
public class RpcServerProvider implements BeanPostProcessor, CommandLineRunner {

    private RpcServer rpcServer;
    private RegistryService registryService;
    private RpcServerProperties rpcServerProperties;

    public RpcServerProvider(RpcServer rpcServer, RegistryService registryService, RpcServerProperties rpcServerProperties){
        this.rpcServer = rpcServer;
        this.registryService = registryService;
        this.rpcServerProperties = rpcServerProperties;
    }

    /**
     * 所有的bean实例化后回调此方法，注册到注册中心
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        LqRpcService lqRpcService = bean.getClass().getAnnotation(LqRpcService.class);
        if (lqRpcService!=null){
            // 存储内存
            String serverName = lqRpcService.interfaceType().getName();
            String version = lqRpcService.version();
            LocalServerCache.store(ServiceNameUtil.serviceKey(serverName,version), bean);

            // 服务注册
            try {
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceName(serverName);
                serviceInfo.setAddress(InetAddress.getLocalHost().getHostAddress());
                serviceInfo.setAppName(rpcServerProperties.getAppName());
                serviceInfo.setPort(String.valueOf(rpcServerProperties.getPort()));
                serviceInfo.setVersion(version);

                registryService.register(serviceInfo);
            } catch (Exception ex) {
                log.error("服务注册出错:{}", ex);
            }
        }
        return bean;
    }

    /**
     * 启动rpc服务
     */
    @Override
    public void run(String... args){
        new Thread(() -> {rpcServer.start(rpcServerProperties.getPort());}).start();
        log.info(" rpc server :{} start, appName :{} , port :{}", rpcServer, rpcServerProperties.getAppName(), rpcServerProperties.getPort());
        // 停止服务时同步注销注册中心中服务
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            try {
                registryService.destroy();
            } catch (IOException e) {
                log.error("", e);
            }
        }));
    }
}
