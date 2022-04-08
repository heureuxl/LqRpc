package com.lq.lqrpc.core.store;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: LocalServerCache
 * @Description: 服务本地缓存
 * @author: liuqi
 * @date: 2022/3/3 下午5:44
 * @Version: 0.0.1
 */
public class LocalServerCache {

    public static final Map<String,Object> serviceCacheMap = new HashMap<>();

    public static Object getService(String serviceName){
        return serviceCacheMap.get(serviceName);
    }

    public static Map<String,Object> getAll(){
        return serviceCacheMap;
    }

    public static void store(String serviceName,Object bean){
        serviceCacheMap.merge(serviceName,bean,(oldValue,newValue) -> oldValue);
    }

}
