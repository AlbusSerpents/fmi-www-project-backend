package com.serpents.ipv6dns.rest;

import com.serpents.ipv6dns.model.UserCredentials;
import com.serpents.ipv6dns.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/status")
@RestController
public class TempEndpoint {

    @Autowired
    private AuthenticationRepository repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public UserCredentials findUser() {
        return repository.findAdmin("pesho", "1234");
    }
}
