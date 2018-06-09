package com.serpents.ipv6dns.domain;

import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.DOMAIN_DETAILS;
import static org.jooq.impl.DSL.inline;

@Repository
public class DomainDetailsRepository {
    private final DSLContext context;

    @Autowired
    public DomainDetailsRepository(final DSLContext context) {
        this.context = context;
    }

    public UUID insert(final DomainDetails details) {
        final Param<String> detailsValue = details.description().map(DSL::inline).orElse(inline((String) null));

        return context.insertInto(DOMAIN_DETAILS.getTable(), DOMAIN_DETAILS.getField(DOMAIN_NAME), DOMAIN_DETAILS.getField(DESCRIPTION))
                      .values(inline(details.getDomainName()), detailsValue)
                      .returning(DOMAIN_DETAILS.getField(ID))
                      .fetchOne()
                      .get(DOMAIN_DETAILS.getField(ID));
    }

    public boolean delete(final UUID id) {
        return context.delete(DOMAIN_DETAILS.getTable())
                      .where(DOMAIN_DETAILS.getField(ID).equal(inline(id)))
                      .execute() == 1;
    }


}
