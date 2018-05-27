package com.serpents.ipv6dns.spring.user.details;

import org.springframework.security.core.GrantedAuthority;

enum GrantedAuthorityImpl implements GrantedAuthority {
    BASE_USER("base"), ADMIN_USER("admin"), CLIENT_USER("client");

    GrantedAuthorityImpl(final String authority) {
        this.authority = authority;
    }

    private final String authority;

    public String getAuthority() {
        return authority;
    }
}
