package com.lq.lqrpc.core.discover;

import com.lq.lqrpc.core.balancer.LoadBalance;
import com.lq.lqrpc.core.common.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class ZookeeperDiscoveryService implements DiscoveryService{

    private static final int BASE_SLEEP_TIME_MS = 1000;
    // 重连次数
    private static final int MAX_RETRIES = 3;
    // zk基本节点
    private static final String ZK_BASE_PATH = "/lqrpc_do";

    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    // 负载均衡算法
    private LoadBalance loadBalance;

    public ZookeeperDiscoveryService(String registryAddr, LoadBalance loadBalance){
        this.loadBalance = loadBalance;
        try{
            // zk连接器
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            client.start();
            // 序列化
            JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
            // 配置服务发现工具
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                    .client(client)
                    .serializer(serializer)
                    .basePath(ZK_BASE_PATH)
                    .build();
            // 开启服务发现服务
            this.serviceDiscovery.start();
        }catch (Exception e){
            log.error("serviceDiscovery start error :{}", e);
        }
    }

    /**
     * 服务发现
     * @param serviceName 服务名
     * @return zk中存储的服务对象信息
     * @throws Exception
     */
    @Override
    public ServiceInfo discover(String serviceName) throws Exception {
        Collection<ServiceInstance<ServiceInfo>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        return CollectionUtils.isEmpty(serviceInstances) ? null :
                loadBalance.chooseService(serviceInstances.stream().map(ServiceInstance::getPayload).collect(Collectors.toList()));

    }
}
