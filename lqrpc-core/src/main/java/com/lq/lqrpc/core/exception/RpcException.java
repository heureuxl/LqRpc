package com.lq.lqrpc.core.exception;

/**
 * @ClassName: RpcException
 * @Description: rpc异常类信息
 * @author: liuqi
 * @date: 2022/3/3 下午5:44
 * @Version: 0.0.1
 */
public class RpcException extends RuntimeException{

    private static final long serialVersionUID = 3365624081242234230L;

    public RpcException() {
        super();
    }

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
