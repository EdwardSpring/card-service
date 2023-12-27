package com.mintyn.cardservice.exception;


public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException() {
        super("Account not found");
    }

    public AccountNotFoundException(String msg) {
        super(msg);
    }
}