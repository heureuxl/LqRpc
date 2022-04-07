package com.lq.lqrpc.client.transport;

import com.lq.lqrpc.client.cache.LocalRpcResponseCache;
import com.lq.lqrpc.client.handler.RpcResponseHandler;
import com.lq.lqrpc.core.codec.RpcDecoder;
import com.lq.lqrpc.core.codec.RpcEncoder;
import com.lq.lqrpc.core.common.RpcRequest;
import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: NettyNetClientTransport
 * @Description: netty客户端发送至netty服务端请求
 * @author: liuqi
 * @date: 2022/4/6 16:29
 * @Version: 0.0.1
 */
@Slf4j
public class NettyNetClientTransport implements NetClientTransport{

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final RpcResponseHandler handler;

    public NettyNetClientTransport() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        handler = new RpcResponseHandler();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new RpcDecoder())
                                .addLast(handler)
                                .addLast(new RpcEncoder<>());
                    }
                });
    }

    /**
     * 与server端netty 发送及接受数据
     * @param metadata 请求元数据
     * @return 协议响应报文
     * @throws Exception
     */
    @Override
    public MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception {
        // 1.获取请求与响应future 存储到本地缓存map中
        MessageProtocol<RpcRequest> messageProtocol = metadata.getMessageProtocol();
        RpcFuture<MessageProtocol<RpcResponse>> future = new RpcFuture<>();
        LocalRpcResponseCache.add(messageProtocol.getHeader().getRequestId(), future);
        // 2.建立tcp连接 远程调用server端
        ChannelFuture channelFuture = bootstrap.connect(metadata.getAddress(), metadata.getPort()).sync();
        // 2.1设置通道监听
        channelFuture.addListener( arg0 -> {
            // 是否调用成功
            if (channelFuture.isSuccess()){
                log.info("connect rpc server {} on port {} success.", metadata.getAddress(), metadata.getPort());
            }else {
                log.error("connect rpc server {} on port {} failed.", metadata.getAddress(), metadata.getPort());
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        // 3.写入数据
        channelFuture.channel().writeAndFlush(messageProtocol);
        // 4.在指定的超时时间内等待获取响应数据
        return metadata.getTimeout() != null ? future.get(metadata.getTimeout(), TimeUnit.MILLISECONDS) : future.get();
    }
}
