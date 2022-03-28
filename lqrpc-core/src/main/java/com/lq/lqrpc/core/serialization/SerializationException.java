package com.lq.lqrpc.core.serialization;

public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 3365624081242234230L;

    public SerializationException(Throwable throwable){
        super(throwable);
    }
}
