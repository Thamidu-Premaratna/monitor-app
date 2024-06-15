package com.monitor.api.exceptions;

public class UserAccessDeniedException extends Exception {
    public UserAccessDeniedException(String message) {
        super(message);
    }
}
