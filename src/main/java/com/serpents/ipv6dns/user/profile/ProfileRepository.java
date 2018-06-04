package com.serpents.ipv6dns.user.profile;

import com.serpents.ipv6dns.credentials.UserRole;
import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest.ChangePasswordRequest;

import java.util.UUID;

public interface ProfileRepository {

    ClientProfile findClient(final UUID id);

    AdminProfile findAdmin(final UUID id);

    void updateClientEmail(final UUID id, final String email);

    void updateAdminEmail(final UUID id, final String email);

    void changePassword(final UUID userId, final UserRole role, final ChangePasswordRequest request);

    void deleteClient(final UUID userId);

}
