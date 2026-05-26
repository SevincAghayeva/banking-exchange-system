package com.pm.bankingexchangesystem.config;
import io.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class LogServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();
        long startTime = System.currentTimeMillis();

        System.out.println("[gRPC İNTERCEPTOR] Yeni sorğu gəldi. Metod: " + methodName);

        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void close(Status status, Metadata trailers) {
                long duration = System.currentTimeMillis() - startTime;
                System.out.println("[gRPC İNTERCEPTOR] Sorğu tamamlandı. Metod: " + methodName
                        + " | Status: " + status.getCode() + " | Sürət: " + duration + " ms");

                super.close(status, trailers);
            }
        }, headers);
    }
}