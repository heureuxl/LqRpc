package com.lq.lqrpc.server.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface LqRpcService {

    /**
     * 暴露服务接口.class
     * @return
     */
    Class<?> interfaceType() default Object.class;

    /**
     * 服务版本
     * @return
     */
    String version() default "1.0";
}
