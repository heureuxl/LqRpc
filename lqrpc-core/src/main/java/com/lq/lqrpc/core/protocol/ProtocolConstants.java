package com.lq.lqrpc.core.protocol;

/**
 * @ClassName: ProtocolConstants
 * @Description: 协议关键字
 * @author: liuqi
 * @date: 2022/3/22 下午5:41
 * @Version: 0.0.1
 */
public class ProtocolConstants {

    // 协议请求头总长
    public static final int HEADER_TOTAL_LEN = 42;

    // 魔数
    public static final short MAGIC = 0x00;

    // 默认版本
    public static final byte VERSION = 0x1;

    // 请求id长度
    public static final int REQ_LEN = 32;

}
