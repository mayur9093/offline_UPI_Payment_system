package com.mayur.offline_UPI_system.exception;

public class WalletAccessDeniedException extends RuntimeException {
    public WalletAccessDeniedException(String message) {
        super(message);
    }
}