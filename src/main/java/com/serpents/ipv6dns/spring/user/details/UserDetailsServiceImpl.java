package com.serpents.ipv6dns.spring.user.details;

import com.serpents.ipv6dns.credentials.UserCredentials;
import com.serpents.ipv6dns.credentials.UserCredentialsRepository;
import com.serpents.ipv6dns.credentials.UserRole;
import com.serpents.ipv6dns.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static com.serpents.ipv6dns.credentials.UserRole.fromToken;
import static com.serpents.ipv6dns.spring.user.details.GrantedAuthorityImpl.*;
import static java.util.Arrays.asList;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialsRepository repository;

    @Autowired
    public UserDetailsServiceImpl(final UserCredentialsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String roleAndName) throws UsernameNotFoundException {
        final Pair<Optional<UserRole>, String> splitUsername = splitUsername(roleAndName);
        final String username = splitUsername.getSecond();
        final UserRole role = splitUsername.getFirst()
                                           .orElseThrow(() -> new UsernameNotFoundException(username));

        switch (role) {
            case CLIENT: {
                final UserCredentials credentials = repository.findClient(username);
                return fromCredentials(credentials, asList(BASE_USER, CLIENT_USER));
            }
            case ADMIN: {
                final UserCredentials credentials = repository.findAdmin(username);
                return fromCredentials(credentials, asList(BASE_USER, ADMIN_USER));
            }
            default:
                throw new RuntimeException("Unreachable");
        }
    }

    private UserDetails fromCredentials(final UserCredentials credentials, final Collection<GrantedAuthority> authorities) {
        return new UserDetailsImpl(credentials.getUserId(), credentials.getUsername(), credentials.getPassword(), authorities);
    }

    private Pair<Optional<UserRole>, String> splitUsername(final String username) {
        final char roleToken = username.charAt(0);
        final Optional<UserRole> role = fromToken(roleToken);
        final String fullUsername = username.substring(1);
        return new Pair<>(role, fullUsername);

    }
}
