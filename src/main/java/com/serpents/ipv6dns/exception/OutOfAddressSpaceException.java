package com.serpents.ipv6dns.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@ResponseStatus(PRECONDITION_FAILED)
public class OutOfAddressSpaceException extends RuntimeException {

}
