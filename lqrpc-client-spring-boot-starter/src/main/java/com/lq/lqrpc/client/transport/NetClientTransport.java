package com.lq.lqrpc.client.transport;

import com.lq.lqrpc.core.protocol.MessageProtocol;

/**
 * @Classname NetClientTransport
 * @Description 网络传输层
 * @Date 2022/3/3 下午5:38
 * @Version: 0.0.1
 */
public interface NetClientTransport {

    /**
     *  发送数据
     * @param metadata
     * @return
     * @throws Exception
     */
    MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception;

}
