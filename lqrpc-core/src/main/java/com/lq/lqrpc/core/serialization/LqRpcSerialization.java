package com.lq.lqrpc.core.serialization;

import java.io.IOException;

public interface LqRpcSerialization {
    /**
     * 序列化
     */
    <T> byte[] serialize(T obj) throws IOException;
    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
