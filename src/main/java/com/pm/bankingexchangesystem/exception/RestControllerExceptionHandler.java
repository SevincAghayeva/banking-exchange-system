package com.pm.bankingexchangesystem.exception;


import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleGrpcException(StatusRuntimeException ex) {

        String grpcCode = ex.getStatus().getCode().name();

        String grpcDescription = ex.getStatus().getDescription();

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        switch (ex.getStatus().getCode()) {
            case INVALID_ARGUMENT:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            case NOT_FOUND:
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            case PERMISSION_DENIED:
                httpStatus = HttpStatus.FORBIDDEN;
                break;
        }

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", grpcCode);
        errorBody.put("message", grpcDescription);
        errorBody.put("status", httpStatus.value());
        errorBody.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorBody, httpStatus);
    }
}