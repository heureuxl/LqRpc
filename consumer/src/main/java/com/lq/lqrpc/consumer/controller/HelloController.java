package com.lq.lqrpc.consumer.controller;

import com.lq.lqrpc.api.HelloService;
import com.lq.lqrpc.client.annotation.LqRpcAutowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: HelloController
 * @Description: TODO
 * @author: liuqi
 * @date: 2022/3/3 下午3:00
 * @Version: 0.0.1
 */
@Controller
public class HelloController {

    @LqRpcAutowired
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello(@RequestParam("name") String name){
        return helloService.sayHello(name);
    }


}
