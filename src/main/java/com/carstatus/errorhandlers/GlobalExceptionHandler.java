package com.carstatus.errorhandlers;

import com.carstatus.services.RequestIdService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces
@Singleton
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse<ErrorMessage>>{

    private final RequestIdService requestIdService;
    @Override
    public HttpResponse<ErrorMessage> handle(HttpRequest request, RuntimeException exception) {
        String requestId = requestIdService.getTraceId();
        log.error(String.format("[%s] - GlobalExceptionHandler thread - %s", requestId,  Thread.currentThread().getName()), exception);
        ErrorMessage errorMsg = ErrorMessage.builder()
                .message(exception.getMessage())
                .requestId(requestId)
                .build();
        return HttpResponse.badRequest(errorMsg);
    }



}
