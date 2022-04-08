package com.lq.lqrpc.core.transport;

import com.lq.lqrpc.core.common.RpcRequest;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: RequestMetadata
 * @Description: 客户端请求元数据
 * @author: liuqi
 * @date: 2022/3/3 下午5:38
 * @Version: 0.0.1
 */
@Data
@Builder
public class RequestMetadata implements Serializable {

    /**
     * 协议
     */
    private MessageProtocol<RpcRequest> messageProtocol;

    /**
     * ip地址
     */
    private String address;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 服务调用超时时长
     */
    private Integer timeout;
}
