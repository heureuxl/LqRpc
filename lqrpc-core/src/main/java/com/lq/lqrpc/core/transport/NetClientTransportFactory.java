package com.lq.lqrpc.core.transport;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: NetClientTransportFactory
 * @Description: 获取nettyclient类
 * @author: liuqi
 * @date: 2022/4/6 16:36
 * @Version: 0.0.1
 */
@Slf4j
public class NetClientTransportFactory {

    public static NetClientTransport getNetClientTransport(){
        return new NettyNetClientTransport();
    }

}
