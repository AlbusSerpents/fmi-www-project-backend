package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.status.Status;
import com.serpents.ipv6dns.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/status")
@RestController
public class StatusEndpoint {

    private final StatusService service;

    @Autowired
    public StatusEndpoint(final StatusService service) {
        this.service = service;
    }

    @ResponseStatus(I_AM_A_TEAPOT)
    @RequestMapping(value = "", method = GET, produces = "application/json")
    public Status status() {
        return service.getStatus();
    }
}
