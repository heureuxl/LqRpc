package com.lq.lqrpc.client.transport;

import com.lq.lqrpc.core.common.RpcResponse;
import com.lq.lqrpc.core.protocol.MessageProtocol;

import java.util.concurrent.*;

/**
 * @ClassName: RpcFuture
 * @Description: 结果异步返回
 * @author: liuqi
 * @date: 2022/4/6 09:39
 * @Version: 0.0.1
 */
public class RpcFuture<T> implements Future<T> {

    /**
     * 响应结果
     */
    private T response;

    /**
     * 因为请求和响应是一一对应的，所以这里是1
     * 使用 CountDownLatch 等待线程
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     * 响应数据若不为空 表示成功
     * @return
     */
    @Override
    public boolean isDone() {
        return this.response != null;
    }

    /**
     *  等待获取数据，直到有结果 也就是 countDownLatch 的值减到 0
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public T get() throws InterruptedException, ExecutionException {
        // 当countDownLatch减为0时，返回下边结果
        countDownLatch.await();
        return response;
    }

    /**
     *  等待固定时长，超时则返回为null
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (countDownLatch.await(timeout, unit)){
            return response;
        }
        return null;
    }

    /**
     * 设置响应结果
     */
    public void setResponse(T messageProtocol){
        this.response = messageProtocol;
        countDownLatch.countDown();
    }


}
