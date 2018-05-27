package com.serpents.ipv6dns.spring.user.details;

import com.serpents.ipv6dns.credentials.UserCredentials;
import com.serpents.ipv6dns.credentials.UserCredentialsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

import static com.serpents.ipv6dns.credentials.UserRole.ADMIN;
import static com.serpents.ipv6dns.credentials.UserRole.CLIENT;
import static com.serpents.ipv6dns.spring.user.details.GrantedAuthorityImpl.*;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl detailsService;

    private UserCredentialsRepository credentialsRepository;

    @Before
    public void setUp() {
        credentialsRepository = mock(UserCredentialsRepository.class);
        detailsService = new UserDetailsServiceImpl(credentialsRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_WrongFormat() {
        final String wrongFormat = " wrong format";

        detailsService.loadUserByUsername(wrongFormat);
    }

    @Test
    public void loadUserByUsername_BlockedClient() {
        final String username = CLIENT.getToken() + "client";

        mockFindClient(true, null);
        final UserDetails details = detailsService.loadUserByUsername(username);

        assertFalse(details.isEnabled());
    }

    @Test
    public void loadUserByUsername_Client() {
        final String username = "client_user";
        final String usernameAndRole = CLIENT.getToken() + username;

        mockFindClient(false, username);
        final UserDetails details = detailsService.loadUserByUsername(usernameAndRole);

        assertEquals(username, details.getUsername());
        assertActiveUserWithUsernameAndAuthorities(details, username, asList(BASE_USER, CLIENT_USER));
    }

    @Test
    public void loadUserByUsername_Admin() {
        final String username = "admin";
        final String usernameAndRole = ADMIN.getToken() + username;

        mockFindAdmin(username);
        final UserDetails details = detailsService.loadUserByUsername(usernameAndRole);

        assertEquals(username, details.getUsername());
        assertActiveUserWithUsernameAndAuthorities(details, username, asList(BASE_USER, ADMIN_USER));
    }

    private void assertActiveUserWithUsernameAndAuthorities(
            final UserDetails details,
            final String expectedUsername,
            final Collection<? extends GrantedAuthority> expectedAuthorities) {
        assertEquals(expectedUsername, details.getUsername());
        assertEquals(expectedAuthorities.size(), details.getAuthorities().size());
        assertTrue(details.getAuthorities().containsAll(expectedAuthorities));
        assertTrue(details.isEnabled());
    }

    private void mockFindAdmin(final String username) {
        when(credentialsRepository.findAdmin(any()))
                .thenReturn(new UserCredentials(randomUUID(), username, "password", false));
    }

    private void mockFindClient(final boolean isBlocked, final String username) {
        when(credentialsRepository.findClient(any()))
                .thenReturn(new UserCredentials(randomUUID(), username, "password", isBlocked));
    }

}