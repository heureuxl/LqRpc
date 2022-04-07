package com.lq.lqrpc.client.cache;

import com.lq.lqrpc.client.transport.RpcFuture;
import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.protocol.MessageProtocol;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: LocalRpcResponseCache
 * @Description: 本地请求与响应信息缓存
 * @author: liuqi
 * @date: 2022/3/3 下午5:36
 * @Version: 0.0.1
 */
public class LocalRpcResponseCache {

    /**
     * 本地请求与响应关系map
     */
    private static Map<String, RpcFuture<MessageProtocol<RpcResponse>>> requestResponseCache = new ConcurrentHashMap<>();

    /**
     * 添加请求和响应future关联关系
     * @param reqId 请求id
     * @param future 响应future
     */
    public static void add(String reqId, RpcFuture<MessageProtocol<RpcResponse>> future){
        requestResponseCache.put(reqId,future);
    }

    /**
     * 向已有的关系数据设置响应值
     * @param reqId
     * @param messageProtocol
     */
    public static void fillResponse(String reqId, MessageProtocol<RpcResponse> messageProtocol){
        // 获取缓存中响应future
        RpcFuture<MessageProtocol<RpcResponse>> future = requestResponseCache.get(reqId);
        // 设置响应结果
        future.setResponse(messageProtocol);
        // 移除缓存
        requestResponseCache.remove(reqId);
    }
}
