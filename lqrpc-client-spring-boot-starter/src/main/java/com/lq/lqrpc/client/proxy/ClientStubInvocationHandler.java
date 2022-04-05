package com.lq.lqrpc.client.proxy;

import com.lq.lqrpc.client.config.RpcClientProperties;
import com.lq.lqrpc.core.common.ServiceInfo;
import com.lq.lqrpc.core.common.ServiceNameUtil;
import com.lq.lqrpc.core.discover.DiscoveryService;
import com.lq.lqrpc.core.exception.ResourceNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName: ClientStubInvocationHandler
 * @Description: client代理实现类 负责通过netty远程调用server端接口，并返回client请求响应结果
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
            throw new ResourceNotFoundException("404");
        }

        // 2.生成请求报文

        // 3.通过netty调用server接口

        // 4.获取响应结果

        // 5.取出body后return

        return null;
    }
}
