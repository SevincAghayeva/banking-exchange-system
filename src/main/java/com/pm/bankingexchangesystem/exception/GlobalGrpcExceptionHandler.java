package com.pm.bankingexchangesystem.exception;

import com.pm.bankingexchangesystem.enums.ErrorMessage;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GlobalGrpcExceptionHandler {

    @GrpcExceptionHandler(BusinessException.class)
    public StatusRuntimeException handleBusinessException(BusinessException ex) {
        ErrorMessage error = ex.getErrorMessage();

        return error.getGrpcStatus()
                .withDescription(error.getDescription())
                .asRuntimeException();
    }

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleGeneralException(Exception ex) {
        ErrorMessage error = ErrorMessage.INTERNAL_SERVER_ERROR;

        return error.getGrpcStatus()
                .withDescription(error.getDescription() + " " + ex.getMessage())
                .asRuntimeException();
    }
}