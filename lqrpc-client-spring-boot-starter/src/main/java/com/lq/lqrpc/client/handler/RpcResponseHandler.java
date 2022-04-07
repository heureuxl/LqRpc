package com.lq.lqrpc.client.handler;

import com.lq.lqrpc.client.cache.LocalRpcResponseCache;
import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.protocol.MessageHeader;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: RpcResponseHandler
 * @Description: netty数据响应处理器
 * @author: liuqi
 * @date: 2022/3/3 下午5:36
 * @Version: 0.0.1
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcResponse> rpcResponseMessageProtocol) throws Exception {
        MessageHeader header = rpcResponseMessageProtocol.getHeader();
        LocalRpcResponseCache.fillResponse(header.getRequestId(), rpcResponseMessageProtocol);
    }
}
