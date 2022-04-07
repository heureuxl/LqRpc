package com.lq.lqrpc.server.handler;

import com.lq.lqrpc.core.common.RpcRequest;
import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.protocol.MessageHeader;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import com.lq.lqrpc.core.protocol.MsgStatus;
import com.lq.lqrpc.core.protocol.MsgType;
import com.lq.lqrpc.server.store.LocalServerCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RpcRequestHandler
 * @Description: netty请求处理器
 * @author: liuqi
 * @date: 2022/3/3 下午5:44
 * @Version: 0.0.1
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcRequest>> {

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcRequest> rpcRequestMessageProtocol) throws Exception {
        // 多线程处理请求
        threadPoolExecutor.submit(() -> {
            MessageProtocol<RpcResponse> messageProtocol = new MessageProtocol<>();
            RpcResponse rpcResponse = new RpcResponse();
            MessageHeader header = rpcRequestMessageProtocol.getHeader();
            // 设置响应请求头
            header.setType(MsgType.RESPONSE.getType());
            // 获取参数调用方法，取得结果后封装响应消息协议
            try{
                Object result = handle(rpcRequestMessageProtocol.getBody());
                rpcResponse.setData(result);
                header.setStatus(MsgStatus.SUCCESS.getCode());
                messageProtocol.setHeader(header);
                messageProtocol.setBody(rpcResponse);
            }catch (Throwable throwable){
                rpcResponse.setMessage(throwable.getMessage());
                header.setStatus(MsgStatus.FAIL.getCode());
                messageProtocol.setBody(rpcResponse);
                messageProtocol.setHeader(header);
                log.error("process request {} error", header.getRequestId(), throwable);
            }
            // 把数据写回去
            channelHandlerContext.writeAndFlush(messageProtocol);
        });
    }

    private Object handle(RpcRequest request){
        try {
            Object bean = LocalServerCache.getService(request.getServiceName());
            if (bean == null) {
                throw new RuntimeException(String.format("service not exist: %s !", request.getServiceName()));
            }
            Method method = bean.getClass().getMethod(request.getMethod(), request.getParamsType());
            return method.invoke(bean, request.getParams());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
