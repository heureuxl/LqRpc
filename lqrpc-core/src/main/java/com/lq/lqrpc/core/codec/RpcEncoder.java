package com.lq.lqrpc.core.codec;

import com.lq.lqrpc.core.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName: RpcEncoder
 * @Description: 自定义编码器
 * @author: liuqi
 * @date: 2022/3/3 下午4:05
 * @Version: 0.0.1
 */
public class RpcEncoder<T> extends MessageToByteEncoder<MessageProtocol<T>> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol<T> tMessageProtocol, ByteBuf byteBuf) throws Exception {

    }
}
