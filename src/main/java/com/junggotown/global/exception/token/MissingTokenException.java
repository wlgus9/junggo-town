package com.junggotown.global.exception.token;

public class MissingTokenException extends TokenException {

    public MissingTokenException() {
        super();
    }

    public MissingTokenException(String message) {
        super(message);
    }
}
