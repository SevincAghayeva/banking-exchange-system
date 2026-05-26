package com.pm.bankingexchangesystem.service;

import com.bank.grpc.exchange.Currency;
import com.bank.grpc.exchange.ExchangeServiceGrpc;
import com.bank.grpc.exchange.RateRequest;
import com.bank.grpc.exchange.RateResponse;
import com.pm.bankingexchangesystem.config.LogServerInterceptor;
import com.pm.bankingexchangesystem.enums.ErrorMessage;
import com.pm.bankingexchangesystem.exception.BusinessException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.Random;

@GrpcService(interceptors = {LogServerInterceptor.class})
public class ExchangeServiceImpl extends ExchangeServiceGrpc.ExchangeServiceImplBase {

    private final Random random = new Random();

    @Override
    public void getRate(RateRequest request, StreamObserver<RateResponse> responseObserver) {
        Currency from = request.getFromCurrency();
        Currency to = request.getToCurrency();

        if (from == Currency.CURRENCY_UNKNOWN || to == Currency.CURRENCY_UNKNOWN) {
            throw new BusinessException(ErrorMessage.INVALID_CURRENCY_CODE);
        }

        if (from == Currency.BTC || to == Currency.BTC) {
            throw new BusinessException(ErrorMessage.CRYPTO_NOT_SUPPORTED);
        }

        RateResponse response = RateResponse.newBuilder()
                .setFromCurrency(from)
                .setToCurrency(to)
                .setRate(1.70)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getLiveRates(RateRequest request, StreamObserver<RateResponse> responseObserver) {
        System.out.println("Canlı məzənnə axını başladı: " + request.getFromCurrency() + " -> " + request.getToCurrency());

        try {
            for (int i = 0; i < 10; i++) {
                double baseRate = 1.70;
                double randomChange = (random.nextDouble() - 0.5) * 0.04;
                double liveRate = Math.round((baseRate + randomChange) * 100.0) / 100.0;

                RateResponse response = RateResponse.newBuilder()
                        .setFromCurrency(request.getFromCurrency())
                        .setToCurrency(request.getToCurrency())
                        .setRate(liveRate)
                        .build();

                responseObserver.onNext(response);

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Stream kəsildi: " + e.getMessage());
        } finally {
            responseObserver.onCompleted();
            System.out.println("Canlı məzənnə axını uğurla başa çatdı.");
        }
    }
}