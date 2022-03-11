package com.lq.lqrpc.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: RpcResponse
 * @Description: 响应内容
 * @author: liuqi
 * @date: 2022/3/11 下午5:40
 * @Version: 0.0.1
 */
@Data
public class RpcResponse implements Serializable {

    private Object data;
    private String message;
}
