package com.serpents.ipv6dns.credentials;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public enum UserRole {
    ADMIN('a'), CLIENT('c');

    UserRole(final char token) {
        this.token = token;
    }

    private final char token;

    public char getToken() {
        return token;
    }

    @JsonCreator
    public static Optional<UserRole> fromToken(final char token) {
        for (UserRole role : UserRole.values()) {
            if (role.token == token) {
                return of(role);
            }
        }
        return empty();
    }

}
