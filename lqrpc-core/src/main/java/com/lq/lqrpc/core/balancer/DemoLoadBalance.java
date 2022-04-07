package com.lq.lqrpc.core.balancer;

import com.lq.lqrpc.core.common.ServiceInfo;

import java.util.List;

/**
 * @ClassName: DemoLoadBalance
 * @Description: 负载均衡算法demo
 * @author: liuqi
 * @date: 2022/3/9 下午4:16
 * @Version: 0.0.1
 */
public class DemoLoadBalance implements LoadBalance{

    @Override
    public ServiceInfo chooseService(List<ServiceInfo> services) {
        if (services != null && services.size()!=0){
            return services.get(0);
        }
        return null;
    }
}
