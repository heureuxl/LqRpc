package com.lq.lqrpc.client.config;

import lombok.Data;

/**
 * @ClassName: RpcClientProperties
 * @Description: 自动装配配置类
 * @author: liuqi
 * @date: 2022/3/3 下午3:40
 * @Version: 0.1
 */
@Data
public class RpcClientProperties {

    // 负载均衡算法
    private String balance;

    // 序列化
    private String serialization;

    // 服务发现地址
    private String discoveryAddr = "";

    // 服务调用超时
    private Integer timeout;
}
