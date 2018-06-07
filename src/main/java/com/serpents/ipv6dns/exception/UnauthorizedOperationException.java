package com.serpents.ipv6dns.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(UNAUTHORIZED)
public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(final String message) {
        super(message);
    }
}
