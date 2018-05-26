package com.serpents.ipv6dns.credentials;

import com.serpents.ipv6dns.credentials.UserCredentials;

public interface UserCredentialsRepository {

    UserCredentials findAdmin(final String username);

    UserCredentials findClient(final String username);

}
