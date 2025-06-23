package com.bytes7.GameHub.exception.custom;

public class TournamentFullException extends RuntimeException {
    public TournamentFullException(String message) {
        super(message);
    }
}
