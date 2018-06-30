package com.serpents.ipv6dns.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String sessionId;
    private UUID id;
    private String username;
}
