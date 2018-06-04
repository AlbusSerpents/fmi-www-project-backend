package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.credentials.UserRole;
import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import com.serpents.ipv6dns.user.profile.Profile;
import com.serpents.ipv6dns.user.profile.ProfileService;
import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.UserDetailsUtils.validateUserId;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/user")
public class ProfileEndpoint {

    private final ProfileService service;

    @Autowired
    public ProfileEndpoint(final ProfileService service) {
        this.service = service;
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/{userId}", method = GET)
    public Profile getProfile(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(name = "userId") UUID userId,
            final @RequestParam(name = "userRole") char roleToken) {
        validateUserId(details, userId);
        final Optional<UserRole> role = UserRole.fromToken(roleToken);
        return service.getProfile(userId, role);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{userId}", method = PUT, consumes = "application/json")
    public void updateProfile(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(name = "userId") UUID userId,
            final @RequestBody @Valid ProfileUpdateRequest request) {
        validateUserId(details, userId);
        service.updateProfile(userId, request);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{userId}", method = DELETE)
    public void deleteProfile(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(name = "userId") UUID userId,
            final HttpSession session) {
        validateUserId(details, userId);
        service.deleteProfile(userId);
        session.invalidate();
    }
}
