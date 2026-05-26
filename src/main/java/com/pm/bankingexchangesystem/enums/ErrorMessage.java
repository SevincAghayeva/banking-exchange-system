package com.pm.bankingexchangesystem.enums;

import io.grpc.Status;

public enum ErrorMessage {

    INVALID_CURRENCY_CODE(Status.INVALID_ARGUMENT, "Zəhmət olmasa düzgün bir valyuta növü seçin!"),
    CRYPTO_NOT_SUPPORTED(Status.NOT_FOUND, "Kriptovalyuta məzənnələri (BTC) müvəqqəti olaraq dəstəklənmir."),
    INTERNAL_SERVER_ERROR(Status.INTERNAL, "Sistem daxili xəta baş verdi.");

    private final Status grpcStatus;
    private final String description;

    ErrorMessage(Status grpcStatus, String description) {
        this.grpcStatus = grpcStatus;
        this.description = description;
    }

    public Status getGrpcStatus() {
        return grpcStatus;
    }

    public String getDescription() {
        return description;
    }
}