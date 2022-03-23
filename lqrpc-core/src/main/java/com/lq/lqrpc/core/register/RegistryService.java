package com.lq.lqrpc.core.register;

import com.lq.lqrpc.core.common.ServiceInfo;

import java.io.IOException;

/**
 * 服务注册和销毁
 */
public interface RegistryService {

    // 注册服务
    void register(ServiceInfo serviceInfo) throws Exception;

    // 销毁服务
    void unRegister(ServiceInfo serviceInfo) throws Exception;

    // 销毁服务
    void destroy() throws IOException;
}
