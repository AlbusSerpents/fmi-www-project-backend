package com.serpents.ipv6dns.spring.user.details;

import com.serpents.ipv6dns.credentials.UserCredentials;
import com.serpents.ipv6dns.credentials.UserCredentialsRepository;
import com.serpents.ipv6dns.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.serpents.ipv6dns.spring.user.details.GrantedAuthorityImpl.*;
import static java.util.Arrays.asList;


@Service
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialsRepository repository;

    @Autowired
    public UserDetailsServiceImpl(final UserCredentialsRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String roleAndName) throws UsernameNotFoundException {
        final Pair<UserCredentials.Role, String> splitUsername = splitUsername(roleAndName);
        final UserCredentials.Role role = splitUsername.getFirst();
        final String username = splitUsername.getSecond();

        switch (role) {
            case CLIENT: {
                final UserCredentials credentials = repository.findClient(username);
                return fromCredentials(credentials, asList(BASE_USER, CLIENT));
            }
            case ADMIN: {
                final UserCredentials credentials = repository.findAdmin(username);
                return fromCredentials(credentials, asList(BASE_USER, ADMINISTRATOR));
            }
            default:
                throw new UsernameNotFoundException(splitUsername.getSecond());
        }
    }

    private UserDetails fromCredentials(final UserCredentials credentials, final Collection<GrantedAuthority> authorities) {
        return new UserDetailsImpl(credentials.getUserId(), credentials.getUsername(), credentials.getPassword(), authorities, credentials.getIsBlocked());
    }

    private Pair<UserCredentials.Role, String> splitUsername(final String username) {
        final char roleToken = username.charAt(0);
        final UserCredentials.Role role = UserCredentials.Role.fromToken(roleToken);
        final String fullUsername = username.substring(1);
        return new Pair<>(role, fullUsername);

    }
}
