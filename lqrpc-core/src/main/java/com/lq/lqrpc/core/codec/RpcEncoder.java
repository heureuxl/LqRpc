package com.lq.lqrpc.core.codec;

import com.lq.lqrpc.core.protocol.MessageHeader;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import com.lq.lqrpc.core.serialization.LqRpcSerialization;
import com.lq.lqrpc.core.serialization.SerializationFactory;
import com.lq.lqrpc.core.serialization.SerializationTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * @ClassName: RpcEncoder
 * @Description: 自定义编码器
 * @author: liuqi
 * @date: 2022/3/3 下午4:05
 * @Version: 0.0.1
 */
public class RpcEncoder<T> extends MessageToByteEncoder<MessageProtocol<T>> {

    /**
     *
     *  +---------------------------------------------------------------+
     *  | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte|
     *  +---------------------------------------------------------------+
     *  | 状态 1byte |        消息 ID 32byte     |      数据长度 4byte    |
     *  +---------------------------------------------------------------+
     *  |                   数据内容 （长度不定）                         |
     *  +---------------------------------------------------------------+
     *
     * 从protocol中读取数据，按照以上规则依次输出信息
     * @param channelHandlerContext
     * @param tMessageProtocol
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol<T> tMessageProtocol, ByteBuf byteBuf) throws Exception {
        MessageHeader header = tMessageProtocol.getHeader();
        // 魔数
        short magic = header.getMagic();
        byteBuf.writeShort(magic);
        // 协议版本号
        byte version = header.getVersion();
        byteBuf.writeByte(version);
        // 序列化算法
        byte serialization = header.getSerialization();
        byteBuf.writeByte(serialization);
        // 报文类型
        byte type = header.getType();
        byteBuf.writeByte(type);
        // 状态
        byte status = header.getStatus();
        byteBuf.writeByte(status);
        // 消息ID
        String requestId = header.getRequestId();
        byteBuf.writeCharSequence(requestId, Charset.forName("UTF-8"));
        // 数据长度
        int msgLen = header.getMsgLen();
        byteBuf.writeInt(msgLen);
        // 序列化数据内容
        LqRpcSerialization seriaization = SerializationFactory.getSeriaization(SerializationTypeEnum.parseType(serialization));
        byte[] data = seriaization.serialize(tMessageProtocol.getBody());
        byteBuf.writeBytes(data);
    }
}
