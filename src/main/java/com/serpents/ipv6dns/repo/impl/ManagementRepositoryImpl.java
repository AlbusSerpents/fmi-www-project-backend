package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.management.ManagementRepository;
import com.serpents.ipv6dns.user.profile.ClientProfile;
import com.serpents.ipv6dns.utils.JooqTable;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqTable.*;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;

@Repository
public class ManagementRepositoryImpl implements ManagementRepository {

    private final DSLContext context;

    @Autowired
    public ManagementRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public List<ClientProfile> findAllClients() {
        return context.select(CLIENT_USERS.getField(ID),
                              CLIENT_USERS.getField(NAME),
                              CLIENT_USERS.getField(EMAIL),
                              CLIENT_USERS.getField(FACULTY_NUMBER))
                      .from(CLIENT_USERS.getTable())
                      .where(CLIENT_USERS.getField(IS_BLOCKED).isFalse())
                      .fetch(record -> new ClientProfile(record.value1(), record.value2(), record.value3(), record.value4()));
    }

    @Override
    public boolean updateIsBlocked(final UUID clientId, final boolean newValue) {
        return context.update(CLIENTS.getTable())
                      .set(row(CLIENTS.getField(IS_BLOCKED)), row(inline(newValue)))
                      .where(CLIENTS.getField(ID).equal(inline(clientId)))
                      .execute() == 1;
    }

    @Override
    public void deleteRequestsByClient(final UUID clientId) {
        context.delete(DOMAIN_REQUESTS.getTable())
               .where(DOMAIN_REQUESTS.getField(CLIENT_ID).equal(inline(clientId)))
               .execute();
    }

    @Override
    public void deleteDomainsByOwner(final UUID ownerId) {
        context.delete(DOMAINS.getTable())
               .where(DOMAINS.getField(OWNER).equal(inline(ownerId)))
               .execute();
    }

    @Override
    public boolean deleteClient(final UUID userId) {
        final boolean deletedFromClients = deleteClientFromTable(CLIENTS, userId);
        return deletedFromClients && deleteClientFromTable(USERS, userId);
    }

    private boolean deleteClientFromTable(final JooqTable schema, final UUID id) {
        return context.delete(schema.getTable())
                      .where(schema.getField(ID).equal(inline(id)))
                      .execute() == 1;
    }


}
