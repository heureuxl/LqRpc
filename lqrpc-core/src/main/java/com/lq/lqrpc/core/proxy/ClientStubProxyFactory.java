package com.lq.lqrpc.core.proxy;

import com.lq.lqrpc.core.config.RpcClientProperties;
import com.lq.lqrpc.core.discover.DiscoveryService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ClientStubProxyFactory
 * @Description: client代理工厂类
 * @author: liuqi
 * @date: 2022/3/3 下午5:37
 * @Version: 0.0.1
 */
public class ClientStubProxyFactory {

    private Map<Class<?>,Object> objectCache = new HashMap<>();

    /**
     * 获取代理对象
     *
     * @param clazz   接口
     * @param version 服务版本
     * @param <T>
     * @return 代理对象
     */
    public <T> T getProxy(Class clazz, String version, DiscoveryService discoveryService, RpcClientProperties rpcClientProperties){
        return (T) objectCache.computeIfAbsent(clazz, clz ->
            Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new ClientStubInvocationHandler(version,discoveryService,clz,rpcClientProperties) )
        );
    }
}
