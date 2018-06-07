package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.domain.DomainDetails;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.DOMAIN_DETAILS;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.DOMAIN_REQUESTS;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;

@Repository
public class DomainRequestsRepositoryImpl implements DomainRequestsRepository {

    private DSLContext context;

    @Autowired
    public DomainRequestsRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public DomainRequest findById(final UUID requestId) {

        return context.select(DOMAIN_REQUESTS.getField(ID),
                              DOMAIN_REQUESTS.getField(STATUS),
                              DOMAIN_REQUESTS.getField(CLIENT_ID),
                              DOMAIN_DETAILS.getField(ID),
                              DOMAIN_DETAILS.getField(DOMAIN_NAME),
                              DOMAIN_DETAILS.getField(DESCRIPTION))
                      .from(DOMAIN_REQUESTS.getTable())
                      .join(DOMAIN_DETAILS.getTable()).on(DOMAIN_REQUESTS.getField(DETAILS_ID).equal(DOMAIN_DETAILS.getField(ID)))
                      .where(DOMAIN_DETAILS.getField(ID).equal(inline(requestId)))
                      .fetchOne(record -> {
                          final DomainDetails domainDetails = new DomainDetails(record.value4(), record.value5(), record.value6());
                          return new DomainRequest(record.value1(), record.value2(), record.value3(), domainDetails);
                      });
    }

    @Override
    public DomainRequestResponse insert(final DomainRequest request) {
        final DomainDetails details = request.getDomainDetails();
        final Param<String> detailsValue = details.getDescription().map(DSL::inline).orElse(inline(""));

        final UUID detailsId =
                context.insertInto(DOMAIN_DETAILS.getTable(), DOMAIN_DETAILS.getField(DOMAIN_NAME), DOMAIN_DETAILS.getField(DESCRIPTION))
                       .values(inline(details.getDomainName()), detailsValue)
                       .returning(DOMAIN_DETAILS.getField(ID))
                       .fetchOne()
                       .get(DOMAIN_DETAILS.getField(ID));

        final UUID requestId =
                context.insertInto(DOMAIN_REQUESTS.getTable(), DOMAIN_REQUESTS.getField(STATUS), DOMAIN_REQUESTS.getField(CLIENT_ID), DOMAIN_REQUESTS.getField(DETAILS_ID))
                       .values(inline(request.getStatus()), inline(request.getClientId()), inline(detailsId))
                       .returning(DOMAIN_REQUESTS.getField(ID))
                       .fetchOne()
                       .get(DOMAIN_REQUESTS.getField(ID));

        return new DomainRequestResponse(requestId);
    }

    @Override
    public boolean updateStatus(final UUID requestId, final DomainRequestStatus newStatus) {
        final int updated =
                context.update(DOMAIN_REQUESTS.getTable())
                       .set(row(DOMAIN_REQUESTS.getField(STATUS)), row(inline(newStatus)))
                       .where(DOMAIN_REQUESTS.getField(ID).equal(inline(requestId)))
                       .execute();

        return updated == 1;
    }
}
