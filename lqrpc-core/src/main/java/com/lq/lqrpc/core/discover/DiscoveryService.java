package com.lq.lqrpc.core.discover;

import com.lq.lqrpc.core.common.ServiceInfo;

public interface DiscoveryService {

    /**
     * 服务发现
     * @param serviceName 服务名
     * @return 从注册中心获取的服务对象信息
     * @throws Exception
     */
    ServiceInfo discover(String serviceName) throws Exception;
}
