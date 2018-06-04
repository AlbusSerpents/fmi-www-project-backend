package com.serpents.ipv6dns.spring.config;

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

import static com.serpents.ipv6dns.spring.user.details.GrantedAuthorityImpl.BASE_USER;
import static com.serpents.ipv6dns.spring.user.details.GrantedAuthorityImpl.CLIENT_USER;
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
        http.formLogin().disable()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(entryPoint)
            .and().authorizeRequests()

            // register client
            .regexMatchers(POST, "/client").permitAll()

            // login
            .regexMatchers(POST, "/auth").permitAll()

            // logout
            .regexMatchers(DELETE, "/auth").hasAuthority(BASE_USER.getAuthority())

            // Delete profile
            .regexMatchers(DELETE,"user/.*").hasAuthority(CLIENT_USER.getAuthority())

            // user profile requests
            .regexMatchers("user/.*").hasAuthority(BASE_USER.getAuthority())

            // common
            .regexMatchers(GET, "/status").permitAll();
    }


}

