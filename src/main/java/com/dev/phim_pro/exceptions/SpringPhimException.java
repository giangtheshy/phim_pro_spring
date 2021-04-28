package com.dev.phim_pro.exceptions;

public class SpringPhimException extends RuntimeException {
    public SpringPhimException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringPhimException(String exMessage) {
        super(exMessage);
    }
}