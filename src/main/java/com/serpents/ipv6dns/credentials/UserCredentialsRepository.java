package com.serpents.ipv6dns.credentials;

public interface UserCredentialsRepository {

    UserCredentials findAdmin(final String username);

    UserCredentials findClient(final String username);

}
