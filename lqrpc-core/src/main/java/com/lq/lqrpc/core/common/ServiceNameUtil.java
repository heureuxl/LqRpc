package com.lq.lqrpc.core.common;

/**
 * @ClassName: ServiceNameUtil
 * @Description: TODO
 * @author: liuqi
 * @date: 2022/3/9 下午5:19
 * @Version: 0.0.1
 */
public class ServiceNameUtil {

    public static String serviceKey(String serviceName,String version){
        return String.join("-",serviceName,version);
    }
}
