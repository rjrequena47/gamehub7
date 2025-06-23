package com.bytes7.GameHub.exception;

import com.bytes7.GameHub.exception.custom.EmailAlreadyExistsException;
import com.bytes7.GameHub.exception.custom.ResourceNotFoundException;
import com.bytes7.GameHub.exception.custom.TournamentFullException;
import com.bytes7.GameHub.exception.custom.UserAlreadyJoinedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleConflictException(ResourceNotFoundException resourceNotFoundException, HttpServletRequest request){
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        resourceNotFoundException.getMessage(),
                        request.getRequestURI()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleUserAlreadyJoinedException(
            UserAlreadyJoinedException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        ex.getMessage(),
                        request.getRequestURI()
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleTournamentFullException(
            TournamentFullException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        ex.getMessage(),
                        request.getRequestURI()
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, HttpServletRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
