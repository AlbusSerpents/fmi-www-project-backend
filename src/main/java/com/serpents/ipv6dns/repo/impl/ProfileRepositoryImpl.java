package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.user.profile.MyProfile;
import com.serpents.ipv6dns.user.profile.ProfileRepository;
import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest.ChangePasswordRequest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqTable.*;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;

@Repository
public class ProfileRepositoryImpl implements ProfileRepository {

    private final DSLContext context;

    @Autowired
    public ProfileRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public MyProfile findMyClientProfileById(final UUID id) {
        return context.select(CLIENT_USERS.getField(ID),
                              CLIENT_USERS.getField(USERNAME),
                              CLIENT_USERS.getField(NAME),
                              CLIENT_USERS.getField(FACULTY_NUMBER),
                              CLIENT_USERS.getField(EMAIL))
                      .from(CLIENT_USERS.getTable())
                      .where(CLIENT_USERS.getField(ID).equal(inline(id)))
                      .fetchOne(record -> new MyProfile(record.value1(), record.value2(), record.value3(), record.value4(), record.value5()));
    }

    @Override
    public boolean updateClientEmail(final UUID id, final String email) {
        return context.update(CLIENTS.getTable())
                      .set(row(CLIENTS.getField(EMAIL)), row(inline(email)))
                      .where(CLIENTS.getField(ID).equal(inline(id)))
                      .execute() == 1;
    }

    @Override
    public boolean changePassword(final UUID userId, final ChangePasswordRequest request) {
        final String newPassword = request.getNewPassword();
        final String originalPassword = request.getOriginalPassword();

        return context.update(USERS.getTable())
                      .set(row(USERS.getField(PASSWORD)), row(inline(newPassword)))
                      .where(USERS.getField(ID).equal(inline(userId))
                                  .and(USERS.getField(PASSWORD).equal(inline(originalPassword))))
                      .execute() == 1;
    }
}
