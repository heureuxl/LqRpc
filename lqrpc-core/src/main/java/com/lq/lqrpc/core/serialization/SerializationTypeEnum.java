package com.lq.lqrpc.core.serialization;


import lombok.Getter;

public enum SerializationTypeEnum {
    DEMO((byte) 0),
    HESSIAN((byte) 1),
    JSON((byte) 2);

    @Getter
    private byte type;

    SerializationTypeEnum(byte type){
        this.type = type;
    }

    public static SerializationTypeEnum parseName(String typeName){
        for (SerializationTypeEnum serializationTypeEnum : SerializationTypeEnum.values()) {
            if (serializationTypeEnum.name().equals(typeName)){
                return serializationTypeEnum;
            }
        }
        return DEMO;
    }

    public static SerializationTypeEnum parseType(byte type){
        for (SerializationTypeEnum serializationTypeEnum : SerializationTypeEnum.values()) {
            if (serializationTypeEnum.type == type){
                return serializationTypeEnum;
            }
        }
        return DEMO;
    }
}
