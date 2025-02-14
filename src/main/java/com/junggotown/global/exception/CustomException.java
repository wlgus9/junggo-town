package com.junggotown.global.exception;

import com.junggotown.global.common.ResponseMessage;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private int code;
    private String message;
    private Throwable throwable;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CustomException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.code = responseMessage.getHttpStatus().value();
        this.message = responseMessage.getMessage();
    }

    public CustomException(ResponseMessage responseMessage, Throwable throwable) {
        super(responseMessage.getMessage());
        this.code = responseMessage.getHttpStatus().value();
        this.throwable = throwable;
    }
}
