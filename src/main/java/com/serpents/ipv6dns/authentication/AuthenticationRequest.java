package com.serpents.ipv6dns.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private Character roleToken;
}
