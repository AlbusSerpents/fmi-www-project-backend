package com.serpents.ipv6dns.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserCredentials {
    private final UUID userId;
    private final String username;
    private final String password;
    private final Boolean isBlocked;

    public enum Role {
        ADMIN('a'), CLIENT('c');

        Role(final char token) {
            this.token = token;
        }

        private final char token;

        public char getToken() {
            return token;
        }

        public static Role fromToken(final char token) {
            for (Role role : Role.values()) {
                if (role.token == token) {
                    return role;
                }
            }
            throw new RuntimeException("Unknown token" + token);
        }

    }
}
