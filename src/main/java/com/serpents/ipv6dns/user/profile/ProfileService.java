package com.serpents.ipv6dns.user.profile;

import com.serpents.ipv6dns.exception.OperationFailedException;
import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public MyProfile getMyProfile(final UUID userId) {
        return repository.findMyClientProfileById(userId);
    }

    @Transactional
    public void updateProfile(final UUID userId, final ProfileUpdateRequest updateRequest) {
        final boolean emailUpdated = updateEmail(userId, updateRequest);
        if (!emailUpdated) {
            throw new OperationFailedException("Couldn't update profile");
        }
        final boolean passwordUpdated = updatePassword(userId, updateRequest);
        if (!passwordUpdated) {
            throw new OperationFailedException("Couldn't update password");
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
        final String email = updateRequest.getEmail();
        return repository.updateClientEmail(userId, email);
    }

    private ChangePasswordRequest encodeRequest(final ChangePasswordRequest request) {
        final String originalPassword = request.getOriginalPassword();
        final String newPassword = request.getNewPassword();
        final String encodedOriginalPassword = encoder.encode(originalPassword);
        final String encodedNewPassword = encoder.encode(newPassword);
        return new ChangePasswordRequest(encodedOriginalPassword, encodedNewPassword);
    }

}
