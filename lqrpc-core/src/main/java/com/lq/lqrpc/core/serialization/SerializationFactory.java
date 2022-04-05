package com.lq.lqrpc.core.serialization;

public class SerializationFactory {

    public static LqRpcSerialization getSeriaization(SerializationTypeEnum serializationTypeEnum){
        switch (serializationTypeEnum) {
            case DEMO:
                return new DemoSerialization();
            case JSON:
                return new JsonSerialization();
            case HESSIAN:
                return new HessianSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal");
        }
    }
}
