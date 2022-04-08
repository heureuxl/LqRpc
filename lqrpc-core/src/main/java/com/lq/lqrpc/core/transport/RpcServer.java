package com.lq.lqrpc.core.transport;

/**
 * 启动底层通讯接口
 */
public interface RpcServer {

    void start(Integer port);
}
