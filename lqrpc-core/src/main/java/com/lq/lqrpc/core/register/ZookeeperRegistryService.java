package com.lq.lqrpc.core.register;

import com.lq.lqrpc.core.common.ServiceInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * @ClassName: ZookeeperRegistryService
 * @Description: 向zookeeper注册服务
 * @author: liuqi
 * @date: 2022/3/4 下午2:06
 * @Version: 0.0.1
 */
public class ZookeeperRegistryService implements RegistryService{

    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    // zk路径
    public static final String ZK_BASE_PATH = "/lqrpc";

    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    public ZookeeperRegistryService(String registryAddr) {
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            client.start();
            JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                    .client(client)
                    .basePath(ZK_BASE_PATH)
                    .serializer(serializer)
                    .build();
            this.serviceDiscovery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .address(ZK_BASE_PATH)
                .name(serviceInfo.getServiceName())
                .port(Integer.parseInt(serviceInfo.getPort()))
                .payload(serviceInfo)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void destroy(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .address(ZK_BASE_PATH)
                .name(serviceInfo.getServiceName())
                .port(Integer.parseInt(serviceInfo.getPort()))
                .payload(serviceInfo)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }
}
