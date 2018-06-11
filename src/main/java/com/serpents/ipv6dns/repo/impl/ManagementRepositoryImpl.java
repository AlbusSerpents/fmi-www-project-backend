package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.management.ManagementRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
