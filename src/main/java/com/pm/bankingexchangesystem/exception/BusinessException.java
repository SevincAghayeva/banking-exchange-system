package com.pm.bankingexchangesystem.exception;


import com.pm.bankingexchangesystem.enums.ErrorMessage;

public class BusinessException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getDescription());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}