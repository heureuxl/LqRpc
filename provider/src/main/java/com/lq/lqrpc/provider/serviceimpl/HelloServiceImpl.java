package com.lq.lqrpc.provider.serviceimpl;

import com.lq.lqrpc.api.HelloService;
import com.lq.lqrpc.server.annotation.LqRpcService;

/**
 * @ClassName: HelloServiceImpl
 * @Description: 服务提供者测试模块
 * @author: liuqi
 * @date: 2022/3/3 下午3:23
 * @Version: 0.0.1
 */
@LqRpcService(interfaceType = HelloService.class,version = "1.0")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        System.out.println("调用成功");
        return name;
    }
}
