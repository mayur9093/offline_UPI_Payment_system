package com.mayur.offline_UPI_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mayur.offline_UPI_system.dto.ErrorResponse;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                exception.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(WalletNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleWalletNotFound(WalletNotFoundException exception) {

                ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                                exception.getMessage(), LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(InsufficientBalanceException.class)
        public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException exception) {

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                exception.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(InvalidAmountException.class)
        public ResponseEntity<ErrorResponse> handleInvalidAmount(
                        InvalidAmountException exception) {

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                exception.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {

                ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                exception.getMessage(), LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {

                String message = exception.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .collect(Collectors.joining(","));

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(), message, LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ErrorResponse> HandleInvalidCredentialsException(InvalidCredentialsException exception) {

                ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                                exception.getMessage(), LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception) {

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.FORBIDDEN.value(),
                                exception.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

}
