package com.lq.lqrpc.core.proxy;

import com.lq.lqrpc.core.config.RpcClientProperties;
import com.lq.lqrpc.core.common.RpcRequest;
import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.common.ServiceInfo;
import com.lq.lqrpc.core.common.ServiceNameUtil;
import com.lq.lqrpc.core.discover.DiscoveryService;
import com.lq.lqrpc.core.exception.ResourceNotFoundException;
import com.lq.lqrpc.core.exception.RpcException;
import com.lq.lqrpc.core.protocol.MessageHeader;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import com.lq.lqrpc.core.protocol.MsgStatus;
import com.lq.lqrpc.core.transport.NetClientTransport;
import com.lq.lqrpc.core.transport.NetClientTransportFactory;
import com.lq.lqrpc.core.transport.RequestMetadata;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName: ClientStubInvocationHandler
 * @Description: client代理实现类 负责通过netty远程调用server端接口，并返回client请求响应结果
 * @author: liuqi
 * @date: 2022/3/3 下午5:37
 * @Version: 0.0.1
 */
@Slf4j
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
        MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>();
        // 2.1 请求头
        messageProtocol.setHeader(MessageHeader.build(rpcClientProperties.getSerialization()));
        // 2.2 请求体
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setMethod(method.getName());
        rpcRequest.setParams(args);
        rpcRequest.setParamsType(method.getParameterTypes());
        rpcRequest.setServiceName(ServiceNameUtil.serviceKey(this.clz.getName(), this.version));
        messageProtocol.setBody(rpcRequest);

        // 3.通过netty调用server接口
        NetClientTransport clientTransport = NetClientTransportFactory.getNetClientTransport();
        MessageProtocol<RpcResponse> rpcResponseMessageProtocol = clientTransport.sendRequest(
                RequestMetadata.builder().messageProtocol(messageProtocol).address(serviceInfo.getAddress()).port(Integer.valueOf(serviceInfo.getPort())).timeout(rpcClientProperties.getTimeout()).build()
        );
        // 4.判断响应结果
        if (rpcResponseMessageProtocol == null){
            log.error("请求超时");
            throw new RpcException("rpc调用结果失败， 请求超时 timeout:" + rpcClientProperties.getTimeout());
        }

        if (!MsgStatus.isSuccess(rpcResponseMessageProtocol.getHeader().getStatus())){
            log.error("rpc调用结果失败， message：{}", rpcResponseMessageProtocol.getBody().getMessage());
            throw new RpcException(rpcResponseMessageProtocol.getBody().getMessage());
        }
        // 5.取出body后return
        return rpcResponseMessageProtocol.getBody().getData();
    }
}
