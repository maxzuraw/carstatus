package com.carstatus.errorhandlers;

import com.carstatus.services.RequestIdService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces
@Singleton
@RequiredArgsConstructor
public class HttpClientResponseExceptionHandler implements ExceptionHandler<HttpClientResponseException, HttpResponse<ErrorMessage>> {

    private final RequestIdService requestIdService;

    @Override
    public HttpResponse<ErrorMessage> handle(HttpRequest request, HttpClientResponseException exception) {
        String serviceId = exception.getServiceId();
        String message = String.format("Call to [%s] failed. Please contact your administrator.", serviceId);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        try{
            status = exception.getStatus();
        } catch(Exception ignored) {
            // NOTE: no status available, non standard exception, maybe there is some hint in message
            message = String.join(" ", message, exception.getMessage());
        }
        String requestId = requestIdService.getTraceId();
        log.error(String.format("[%s] - HttpClientResponseExceptionHandler thread - %s", requestId,  Thread.currentThread().getName()), exception);
        ErrorMessage errorMsg = ErrorMessage.builder()
                .message(message)
                .requestId(requestId)
                .build();
        return HttpResponse.serverError(errorMsg).status(status);
    }
}
