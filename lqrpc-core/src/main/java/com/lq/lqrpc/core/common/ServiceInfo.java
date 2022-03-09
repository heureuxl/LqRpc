package com.lq.lqrpc.core.common;

import lombok.Data;

/**
 * @ClassName: ServiceInfo
 * @Description: 服务信息
 * @author: liuqi
 * @date: 2022/3/3 下午4:03
 * @Version: 0.0.1
 */
@Data
public class ServiceInfo {

    // 应用名称
    private String appName;

    // 服务名
    private String serviceName;

    // 地址
    private String address;

    // 版本
    private String version;

    // 端口
    private String port;
}
