package com.lq.lqrpc.core.protocol;

import com.lq.lqrpc.core.serialization.SerializationTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @ClassName: MessageHeader
 * @Description: 消息请求头
 * @author: liuqi
 * @date: 2022/3/10 下午4:07
 * @Version: 0.0.1
 */
@Data
public class MessageHeader implements Serializable {

    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 32byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    */
    /**
     * 魔数
     */
    private short magic;

    /**
     * 协议版本号
     */
    private byte version;

    /**
     * 序列化算法
     */
    private byte serialization;

    /**
     * 报文类型
     */
    private byte type;

    /**
     * 状态
     */
    private byte status;

    /**
     * 消息ID
     */
    private String requestId;

    /**
     * 数据长度
     */
    private int msgLen;

    public static MessageHeader build(String serialization){
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMagic(ProtocolConstants.MAGIC);
        messageHeader.setVersion(ProtocolConstants.VERSION);
        messageHeader.setRequestId(UUID.randomUUID().toString().replaceAll("-",""));
        messageHeader.setType(MsgType.REQUEST.getType());
        messageHeader.setSerialization(SerializationTypeEnum.parseName(serialization).getType());
        return messageHeader;
    }
}
