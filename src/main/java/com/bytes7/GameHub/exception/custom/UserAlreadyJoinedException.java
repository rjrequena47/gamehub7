package com.bytes7.GameHub.exception.custom;

public class UserAlreadyJoinedException extends RuntimeException {
    public UserAlreadyJoinedException(String message) {
        super(message);
    }
}