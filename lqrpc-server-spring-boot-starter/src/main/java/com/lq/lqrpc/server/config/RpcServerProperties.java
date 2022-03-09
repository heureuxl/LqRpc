package com.lq.lqrpc.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: RpcServerProperties
 * @Description: 自动装配配置类
 * @author: liuqi
 * @date: 2022/3/3 下午3:39
 * @Version: 0.1
 */
@Data
@ConfigurationProperties(prefix = "lqrpc.server")
public class RpcServerProperties {

    // 服务启动端口
    private Integer port = 5000;

    // 注册中心地址
    private String registerAddress;

    // 服务名
    private String appName;
}
