package com.serpents.ipv6dns.user.profile;

import com.serpents.ipv6dns.credentials.UserRole;
import com.serpents.ipv6dns.exception.BadRequestException;
import com.serpents.ipv6dns.exception.OperationFailedException;
import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public ProfileService(final ProfileRepository repository, final PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public Profile getProfile(final UUID userId, final Optional<UserRole> optionalRole) {
        final UserRole role = getRole(optionalRole);
        switch (role) {
            case ADMIN:
                return repository.findAdmin(userId);
            case CLIENT:
                return repository.findClient(userId);
            default:
                throw new RuntimeException("Unreachable");
        }
    }

    @Transactional
    public void updateProfile(final UUID userId, final ProfileUpdateRequest updateRequest) {
        if (!updatePassword(userId, updateRequest)) {
            throw new OperationFailedException("Couldn't update password");
        }
        if (!updateEmail(userId, updateRequest)) {
            throw new OperationFailedException("Couldn't update profile");
        }
    }

    private boolean updatePassword(final UUID userId, final ProfileUpdateRequest updateRequest) {
        return updateRequest
                .getPasswordRequest()
                .map(this::encodeRequest)
                .map(request -> repository.changePassword(userId, request))
                .orElse(true);
    }

    private boolean updateEmail(final UUID userId, final ProfileUpdateRequest updateRequest) {
        final UserRole role = getRole(updateRequest.getRole());
        final String email = updateRequest.getEmail();
        switch (role) {
            case ADMIN:
                return repository.updateAdminEmail(userId, email);
            case CLIENT:
                return repository.updateClientEmail(userId, email);
            default:
                throw new RuntimeException("Unreachable");
        }
    }

    private UserRole getRole(final Optional<UserRole> optionalRole) {
        return optionalRole.orElseThrow(() -> new BadRequestException("Unknown role"));
    }

    private ChangePasswordRequest encodeRequest(final ChangePasswordRequest request) {
        final String originalPassword = request.getOriginalPassword();
        final String newPassword = request.getNewPassword();
        final String encodedOriginalPassword = encoder.encode(originalPassword);
        final String encodedNewPassword = encoder.encode(newPassword);
        return new ChangePasswordRequest(encodedOriginalPassword, encodedNewPassword);
    }

    @Transactional
    public void deleteProfile(final UUID userId) {
        final boolean success = repository.deleteClient(userId);
        if (!success) {
            final String message = String.format("Couldn't delete profile with id: %s", userId);
            throw new OperationFailedException(message);
        }
    }
}
