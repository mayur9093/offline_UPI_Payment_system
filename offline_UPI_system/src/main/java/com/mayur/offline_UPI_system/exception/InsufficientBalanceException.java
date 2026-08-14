package com.mayur.offline_UPI_system.exception;



public class InsufficientBalanceException extends RuntimeException{
        public InsufficientBalanceException(String message){
            super(message);
        }
}
