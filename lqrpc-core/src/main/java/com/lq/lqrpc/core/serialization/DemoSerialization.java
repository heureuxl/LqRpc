package com.lq.lqrpc.core.serialization;

import java.io.*;

/**
 * @ClassName: DemoSerialization
 * @Description: 序列化Demo
 * @author: liuqi
 * @date: 2022/3/9 下午4:24
 * @Version: 0.0.1
 */
public class DemoSerialization implements LqRpcSerialization{

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
