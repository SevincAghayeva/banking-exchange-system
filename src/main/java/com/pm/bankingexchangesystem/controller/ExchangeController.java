package com.pm.bankingexchangesystem.controller;

import com.bank.grpc.exchange.Currency;
import com.bank.grpc.exchange.ExchangeServiceGrpc;
import com.bank.grpc.exchange.RateRequest;
import com.bank.grpc.exchange.RateResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ExchangeController {


    @GrpcClient("exchangeClient")
    private ExchangeServiceGrpc.ExchangeServiceBlockingStub exchangeStub;

    @GetMapping("/api/rate")
    public Map<String, Object> getExchangeRate(@RequestParam String from, @RequestParam String to) {

        Currency fromEnum = Currency.valueOf(from.toUpperCase());
        Currency toEnum = Currency.valueOf(to.toUpperCase());

        RateRequest request = RateRequest.newBuilder()
                .setFromCurrency(fromEnum)
                .setToCurrency(toEnum)
                .build();

        RateResponse response = this.exchangeStub.getRate(request);

        Map<String, Object> result = new HashMap<>();
        result.put("from", response.getFromCurrency().toString());
        result.put("to", response.getToCurrency().toString());
        result.put("rate", response.getRate());
        result.put("message", "gRPC Serverindən uğurla gəldi!");

        return result;
    }
}