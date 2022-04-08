package com.lq.lqrpc.consumer.controller;

import com.lq.lqrpc.api.HelloService;
import com.lq.lqrpc.core.annotation.LqRpcAutowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: HelloController
 * @Description: TODO
 * @author: liuqi
 * @date: 2022/3/3 下午3:00
 * @Version: 0.0.1
 */
@RestController
public class HelloController {

    @LqRpcAutowired(version = "1.0")
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello(@RequestParam("name") String name){
        return helloService.sayHello(name);
    }


}
