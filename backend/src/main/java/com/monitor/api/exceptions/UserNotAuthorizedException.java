package com.monitor.api.exceptions;

public class UserNotAuthorizedException extends Exception {
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
