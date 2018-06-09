package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.request.DomainRequest.Identifier;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.domain.request.DomainRequestStatus.APPROVED;
import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.DOMAIN_DETAILS;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.DOMAIN_REQUESTS;
import static com.serpents.ipv6dns.utils.TimeUtils.nowAtUtc;
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
    public DomainRequest findByIdentifier(final Identifier identifier) {
        return findByCondition(identifierCondition(identifier));
    }

    @Override
    public DomainRequest findById(final UUID id) {
        return findByCondition(idCondition(id));
    }

    @Override
    public DomainRequestResponse insert(final DomainRequest request) {
        final DomainDetails details = request.getDomainDetails();
        final Param<String> detailsValue = details.description().map(DSL::inline).orElse(inline((String) null));

        final UUID detailsId =
                context.insertInto(DOMAIN_DETAILS.getTable(), DOMAIN_DETAILS.getField(DOMAIN_NAME), DOMAIN_DETAILS.getField(DESCRIPTION))
                       .values(inline(details.getDomainName()), detailsValue)
                       .returning(DOMAIN_DETAILS.getField(ID))
                       .fetchOne()
                       .get(DOMAIN_DETAILS.getField(ID));

        final UUID requestId =
                context.insertInto(DOMAIN_REQUESTS.getTable(), DOMAIN_REQUESTS.getField(STATUS), DOMAIN_REQUESTS.getField(CLIENT_ID), DOMAIN_REQUESTS.getField(DETAILS_ID))
                       .values(inline(request.getStatus().name()), inline(request.getClientId()), inline(detailsId))
                       .returning(DOMAIN_REQUESTS.getField(ID))
                       .fetchOne()
                       .get(DOMAIN_REQUESTS.getField(ID));

        return new DomainRequestResponse(requestId);
    }

    @Override
    public boolean approve(final UUID requestId) {
        final int updated =
                context.update(DOMAIN_REQUESTS.getTable())
                       .set(row(DOMAIN_REQUESTS.getField(STATUS), DOMAIN_REQUESTS.getField(UPDATED_AT)),
                            row(inline(APPROVED.name()), inline(nowAtUtc())))
                       .where(DOMAIN_REQUESTS.getField(ID).equal(inline(requestId)))
                       .execute();

        return updated == 1;
    }

    @Override
    public boolean reject(final UUID id) {
        return deleteRequestByCondition(idCondition(id));
    }

    @Override
    public boolean cancel(final Identifier identifier) {
        return deleteRequestByCondition(identifierCondition(identifier));
    }

    private boolean deleteRequestByCondition(final Condition condition) {
        final Optional<UUID> detailsId =
                context.delete(DOMAIN_REQUESTS.getTable())
                       .where(condition)
                       .returning(DOMAIN_REQUESTS.getField(DETAILS_ID))
                       .fetchOptional()
                       .map(result -> result.get(DOMAIN_REQUESTS.getField(DETAILS_ID)));

        return detailsId.map(this::deleteDetails).orElse(false);
    }

    private boolean deleteDetails(final UUID detailsId) {
        return context.delete(DOMAIN_DETAILS.getTable())
                      .where(DOMAIN_DETAILS.getField(ID).equal(inline(detailsId)))
                      .execute() == 1;
    }

    private DomainRequest findByCondition(final Condition condition) {
        return context.select(DOMAIN_REQUESTS.getField(ID),
                              DOMAIN_REQUESTS.getField(STATUS),
                              DOMAIN_REQUESTS.getField(CLIENT_ID),
                              DOMAIN_DETAILS.getField(ID),
                              DOMAIN_DETAILS.getField(DOMAIN_NAME),
                              DOMAIN_DETAILS.getField(DESCRIPTION))
                      .from(DOMAIN_REQUESTS.getTable())
                      .join(DOMAIN_DETAILS.getTable()).on(DOMAIN_REQUESTS.getField(DETAILS_ID).equal(DOMAIN_DETAILS.getField(ID)))
                      .where(condition)
                      .fetchOne(record -> {
                          final DomainDetails domainDetails = new DomainDetails(record.value4(), record.value5(), record.value6());
                          return new DomainRequest(record.value1(), DomainRequestStatus.valueOf(record.value2()), record.value3(), domainDetails);
                      });

    }

    private Condition idCondition(final UUID id) {
        return DOMAIN_REQUESTS.getField(ID).equal(inline(id));
    }

    private Condition identifierCondition(final Identifier identifier) {
        return DOMAIN_REQUESTS.getField(ID).equal(inline(identifier.getRequestId()))
                              .and(DOMAIN_REQUESTS.getField(CLIENT_ID).equal(identifier.getClientId()));
    }
}
