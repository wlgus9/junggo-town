package com.junggotown.global.exception.token;

public class InvalidTokenException extends TokenException{
    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
