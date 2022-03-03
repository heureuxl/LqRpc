package com.lq.lqrpc.core.balancer;

import com.lq.lqrpc.core.common.ServiceInfo;

import java.util.List;

/**
 * 负载均衡算法
 */
public interface LoadBalance {

    /**
     * 获取服务
     * @param services 服务列表
     * @return 服务
     */
    ServiceInfo chooseService(List<ServiceInfo> services);
}
