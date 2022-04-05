package com.lq.lqrpc.core.codec;

import com.lq.lqrpc.core.common.RpcRequest;
import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.protocol.MessageHeader;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import com.lq.lqrpc.core.protocol.MsgType;
import com.lq.lqrpc.core.protocol.ProtocolConstants;
import com.lq.lqrpc.core.serialization.LqRpcSerialization;
import com.lq.lqrpc.core.serialization.SerializationFactory;
import com.lq.lqrpc.core.serialization.SerializationTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @ClassName: RpcDecoder
 * @Description: 自定义解码器
 * @author: liuqi
 * @date: 2022/3/3 下午4:05
 * @Version: 0.0.1
 */
public class RpcDecoder<T> extends ByteToMessageDecoder {

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
     * 从读到的输入流中按以上规则依次读取数据，封装protocol输出，交给nettyhandler处理
     * @param channelHandlerContext
     * @param out
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN){
            // 可读的数据小于请求头总的大小 直接丢弃
            return;
        }

        // 魔数
        short magic = byteBuf.readShort();
        if (magic != ProtocolConstants.MAGIC ){
            return;
        }

        byte version = byteBuf.readByte();
        byte serializeType = byteBuf.readByte();
        byte type = byteBuf.readByte();
        byte status = byteBuf.readByte();

        CharSequence requestId = byteBuf.readCharSequence(ProtocolConstants.REQ_LEN, Charset.forName("UTF-8"));

        int dataLength = byteBuf.readInt();
        // 如果读取数据少于数据长度 丢弃
        if (byteBuf.readableBytes() < dataLength){
            byteBuf.resetReaderIndex();
            return;
        }

        // 读取数据(消息体)
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        // 判断请求类型是否符合要求
        MsgType msgType = MsgType.findByType(type);
        if (msgType == null){
            return;
        }

        // 开始将读取字节数据转换成协议规范的消息对象
        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setMsgLen(dataLength);
        header.setRequestId(String.valueOf(requestId));
        header.setSerialization(serializeType);
        header.setStatus(status);

        // 序列化数据(消息体)并放入协议规范的消息对象中
        LqRpcSerialization seriaization = SerializationFactory.getSeriaization(SerializationTypeEnum.parseType(serializeType));
        // 根据不同消息类型(请求/响应) 把请求数据序列化成对应消息类型的对象
        switch (msgType){
            case REQUEST:
                RpcRequest request = seriaization.deserialize(data, RpcRequest.class);
                if (request != null){
                    MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>();
                    messageProtocol.setHeader(header);
                    messageProtocol.setBody(request);
                    out.add(messageProtocol);
                }
                break;
            case RESPONSE:
                RpcResponse response = seriaization.deserialize(data, RpcResponse.class);
                if (response != null){
                    MessageProtocol<RpcResponse> messageProtocol = new MessageProtocol<>();
                    messageProtocol.setHeader(header);
                    messageProtocol.setBody(response);
                    out.add(response);
                }
        }
    }
}
