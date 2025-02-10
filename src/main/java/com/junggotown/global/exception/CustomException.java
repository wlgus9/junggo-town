package com.junggotown.global.exception;

import com.junggotown.global.common.ResponseMessage;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ResponseMessage responseMessage;
    private Throwable throwable;

    public CustomException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public CustomException(ResponseMessage responseMessage, Throwable throwable) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
        this.throwable = throwable;
    }
}
