package com.serpents.ipv6dns.spring.security;

import org.springframework.security.core.GrantedAuthority;

public enum GrantedAuthorityImpl implements GrantedAuthority {
    BASE_USER("base"), ADMINISTRATOR("admin"), CLIENT("client");

    GrantedAuthorityImpl(final String authority) {
        this.authority = authority;
    }

    private final String authority;

    public String getAuthority() {
        return authority;
    }
}
