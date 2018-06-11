package com.serpents.ipv6dns.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class InvalidDomainNameException extends RuntimeException {
    public InvalidDomainNameException(final String message) {
        super(message);
    }
}
