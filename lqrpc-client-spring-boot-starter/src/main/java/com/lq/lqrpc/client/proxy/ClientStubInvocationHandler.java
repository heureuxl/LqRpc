package com.lq.lqrpc.client.proxy;

import com.lq.lqrpc.client.config.RpcClientProperties;
import com.lq.lqrpc.core.common.ServiceInfo;
import com.lq.lqrpc.core.common.ServiceNameUtil;
import com.lq.lqrpc.core.discover.DiscoveryService;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName: ClientStubInvocationHandler
 * @Description: TODO
 * @author: liuqi
 * @date: 2022/3/3 下午5:37
 * @Version: 0.0.1
 */
public class ClientStubInvocationHandler implements InvocationHandler {

    private String version;

    private DiscoveryService discoveryService;

    private Class<?> clz;

    private RpcClientProperties rpcClientProperties;

    public ClientStubInvocationHandler(String version,DiscoveryService discoveryService,Class<?> clz,RpcClientProperties rpcClientProperties) {
        super();
        this.clz = clz;
        this.version = version;
        this.discoveryService = discoveryService;
        this.rpcClientProperties = rpcClientProperties;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1.获取服务信息
        ServiceInfo serviceInfo = discoveryService.discover(ServiceNameUtil.serviceKey(clz.getName(), version));
        if (serviceInfo == null){
            throw new ConfigDataResourceNotFoundException();
        }





        return null;
    }
}
