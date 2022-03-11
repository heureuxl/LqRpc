package com.lq.lqrpc.core.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: MessageProtocol
 * @Description: 消息协议
 * @author: liuqi
 * @date: 2022/3/3 下午5:38
 * @Version: 0.0.1
 */
@Data
public class MessageProtocol<T> implements Serializable {

    // 消息头
    private MessageHeader header;

    private T body;
}
