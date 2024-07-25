package com.org.peerrecognition.exception;


public class BadCredentialsException extends RuntimeException {
    private String message;
    public BadCredentialsException(String message) {
        super();
        this.message = message;
    }
}
