package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.credentials.UserRole;
import com.serpents.ipv6dns.user.profile.AdminProfile;
import com.serpents.ipv6dns.user.profile.ClientProfile;
import com.serpents.ipv6dns.user.profile.ProfileRepository;
import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest.ChangePasswordRequest;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.function.Function;

import static org.jooq.impl.DSL.*;

@Repository
public class ProfileRepositoryImpl implements ProfileRepository {

    private final Field<UUID> idField = field("id", UUID.class);
    private final Field<String> usernameField = field("username", String.class);
    private final Field<String> nameField = field("name", String.class);
    private final Field<String> emailField = field("email", String.class);

    private final Table<?> adminsTable = table("admins");
    private final Table<?> clientsTable = table("clients");
    private final Table<?> usersTable = table("users");

    private final Table<?> clientUsersView = table("client_users");
    private final Table<?> adminUsersView = table("admin_users");

    private final DSLContext context;

    @Autowired
    public ProfileRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public ClientProfile findClient(final UUID id) {

        final Field<Integer> facultyNumberField = field("faculty_number", Integer.class);

        return context.select(idField, usernameField, nameField, facultyNumberField, emailField)
                      .from(clientUsersView)
                      .where(idField.equal(inline(id)))
                      .fetchOne(record -> new ClientProfile(record.value1(), record.value2(), record.value3(), record.value4(), record.value5()));
    }

    @Override
    public AdminProfile findAdmin(final UUID id) {

        return context.select(idField, usernameField, nameField, emailField)
                      .from(adminUsersView)
                      .where(idField.equal(inline(id)))
                      .fetchOne(record -> new AdminProfile(record.value1(), record.value2(), record.value3(), record.value4()));
    }

    @Override
    public void updateClientEmail(final UUID id, final String email) {
        updateEmailFieldForTable(clientsTable, id, email);
    }

    @Override
    public void updateAdminEmail(final UUID id, final String email) {
        updateEmailFieldForTable(adminsTable, id, email);
    }

    private void updateEmailFieldForTable(final Table<?> table, final UUID id, final String email) {

        final Field<UUID> idField = field("id", UUID.class);
        final Field<String> emailField = field("email", String.class);

        context.update(table)
               .set(row(emailField), row(inline(email)))
               .where(idField.equal(inline(id)))
               .execute();
    }

    @Override
    public void changePassword(final UUID userId, final UserRole role, final ChangePasswordRequest request) {
        final Table<?> extraUsersTable = extraUsersTableForRole(role);


        final Field<UUID> idField = field("id", UUID.class);
        final Field<String> passwordField = field("password", String.class);

        final Field<UUID> idValues = context
                .select(idField)
                .from(extraUsersTable)
                .asField();

        final String newPassword = request.getNewPassword();
        final String originalPassword = request.getOriginalPassword();

        context.update(usersTable)
               .set(row(passwordField), row(inline(newPassword)))
               .where(idField.equal(inline(userId))
                             .and(passwordField.equal(inline(originalPassword)))
                             .and(inline(userId).in(idValues)))
               .execute();
    }

    private Table<?> extraUsersTableForRole(final UserRole role) {
        switch (role) {
            case CLIENT:
                return clientsTable;
            case ADMIN:
                return adminsTable;
            default:
                return null;
        }
    }

    @Override
    public void deleteClient(final UUID userId) {
        final Function<Table<?>, Integer> deleteFromTableById = table -> context.delete(table)
                                                                                .where(idField.equal(inline(userId)))
                                                                                .execute();
        deleteFromTableById.apply(clientsTable);
        deleteFromTableById.apply(usersTable);
    }
}
