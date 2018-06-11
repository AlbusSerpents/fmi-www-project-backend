package com.serpents.ipv6dns.user.profile;

import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest.ChangePasswordRequest;

import java.util.UUID;

public interface ProfileRepository {

    ClientMyProfile findMyClientProfileById(final UUID id);

    ClientProfile findClientById(final UUID id);

    AdminMyProfile findMyAdminProfileById(final UUID id);

    Profile findAdminById(final UUID id);

    boolean updateClientEmail(final UUID id, final String email);

    boolean updateAdminEmail(final UUID id, final String email);

    boolean changePassword(final UUID userId, final ChangePasswordRequest request);

}
