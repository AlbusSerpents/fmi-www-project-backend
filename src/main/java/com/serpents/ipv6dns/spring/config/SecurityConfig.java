package com.serpents.ipv6dns.spring.config;

import com.serpents.ipv6dns.spring.networking.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.session.SessionManagementFilter;

import static com.serpents.ipv6dns.spring.user.details.GrantedAuthorityImpl.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService detailsService;
    private final AuthenticationEntryPoint entryPoint;
    private final PasswordEncoder encoder;

    @Autowired
    public SecurityConfig(
            final UserDetailsService detailsService,
            final AuthenticationEntryPoint entryPoint,
            final PasswordEncoder encoder) {
        this.detailsService = detailsService;
        this.entryPoint = entryPoint;
        this.encoder = encoder;
    }

    @Autowired
    public void configurationGlobal(final AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(detailsService).passwordEncoder(encoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new CORSFilter(), SessionManagementFilter.class)
                .formLogin().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and().authorizeRequests()

                // register client
                .regexMatchers(POST, "/client").permitAll()

                // login
                .regexMatchers(POST, "/auth").permitAll()

                // logout
                .regexMatchers(DELETE, "/auth").hasAuthority(BASE_USER.getAuthority())

                // Addresses
                .regexMatchers("/addresses.*").hasAnyAuthority(ADMIN_USER.getAuthority())

                // Create, read or cancel request
                .regexMatchers(POST, "/request").hasAnyAuthority(CLIENT_USER.getAuthority())
                .regexMatchers(DELETE, "/request/.*").hasAnyAuthority(CLIENT_USER.getAuthority())
                .regexMatchers(GET, "/request/.*").hasAnyAuthority(CLIENT_USER.getAuthority())

                // Read pending, reject and approve request
                .regexMatchers(GET, "/request").hasAnyAuthority(ADMIN_USER.getAuthority())
                .regexMatchers(POST, "/request/.*/approve").hasAnyAuthority(ADMIN_USER.getAuthority())
                .regexMatchers(POST, "/request/.*/reject").hasAnyAuthority(ADMIN_USER.getAuthority())

                // Manage accounts
                .regexMatchers("/manage.*").hasAuthority(ADMIN_USER.getAuthority())

                // Get domains and get by id
                .regexMatchers(GET, "/domain?.*").hasAnyAuthority(BASE_USER.getAuthority())
                .regexMatchers(GET, "/domain/view/.*").hasAnyAuthority(BASE_USER.getAuthority())

                // Get all domains
                .regexMatchers(GET, "/domain/all.*").hasAnyAuthority(ADMIN_USER.getAuthority())

                // Get my domains
                .regexMatchers(GET, "/domain/my-domains/.*").hasAnyAuthority(CLIENT_USER.getAuthority())

                // user profile
                .regexMatchers(GET,"/user/.*").hasAuthority(BASE_USER.getAuthority())
                .regexMatchers(PUT,"/user/.*").hasAuthority(BASE_USER.getAuthority())

                // common
                .regexMatchers(GET, "/status").permitAll();
    }


}

