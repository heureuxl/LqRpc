package com.lq.lqrpc.core.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {

    // 请求服务名
    private String serviceName;

    // 请求方法
    private String method;

    // 请求参数类型
    private Class<?>[] paramsType;

    // 请求参数
    private Object[] params;
}
