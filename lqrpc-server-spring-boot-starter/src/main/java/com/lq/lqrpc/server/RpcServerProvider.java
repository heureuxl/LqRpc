package com.lq.lqrpc.server;

import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @ClassName: RpcServerProvider
 * @Description: 实例化bean后进行服务注册，并且当服务销毁后同步删除注册中心中服务
 * @author: liuqi
 * @date: 2022/3/9 下午5:26
 * @Version: 0.0.1
 */
public class RpcServerProvider implements BeanPostProcessor {
}
