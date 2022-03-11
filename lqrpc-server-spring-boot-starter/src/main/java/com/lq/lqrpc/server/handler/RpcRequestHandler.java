package com.lq.lqrpc.server.handler;

import com.lq.lqrpc.core.common.RpcRequest;
import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

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
        threadPoolExecutor.submit(() -> {
            MessageProtocol<RpcResponse> messageProtocol = new MessageProtocol<>();
            RpcResponse rpcResponse = new RpcResponse();
        });
    }
}
