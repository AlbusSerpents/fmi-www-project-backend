package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.DomainDetailsRepository;
import com.serpents.ipv6dns.domain.request.DomainRequest;
import com.serpents.ipv6dns.domain.request.DomainRequest.Identifier;
import com.serpents.ipv6dns.domain.request.DomainRequestResponse;
import com.serpents.ipv6dns.domain.request.DomainRequestStatus;
import com.serpents.ipv6dns.domain.request.DomainRequestsRepository;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.domain.request.DomainRequestStatus.APPROVED;
import static com.serpents.ipv6dns.domain.request.DomainRequestStatus.SENT;
import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.DOMAIN_DETAILS;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.DOMAIN_REQUESTS;
import static com.serpents.ipv6dns.utils.TimeUtils.nowAtUtc;
import static org.jooq.impl.DSL.*;

@Repository
public class DomainRequestsRepositoryImpl implements DomainRequestsRepository {

    private final RecordMapper<Record6<UUID, String, UUID, UUID, String, String>, DomainRequest> MAPPER = record -> {
        final DomainDetails domainDetails = new DomainDetails(record.value4(), record.value5(), record.value6());
        return new DomainRequest(record.value1(), DomainRequestStatus.valueOf(record.value2()), record.value3(), domainDetails);
    };

    private final DSLContext context;
    private final DomainDetailsRepository detailsRepository;

    @Autowired
    public DomainRequestsRepositoryImpl(final DSLContext context, final DomainDetailsRepository detailsRepository) {
        this.context = context;
        this.detailsRepository = detailsRepository;
    }

    @Override
    public List<DomainRequest> findPending() {
        return selectByCondition(stausEquals(SENT)).fetch(MAPPER);
    }

    @Override
    public DomainRequest findById(final UUID id) {
        return selectByCondition(idEquals(id)).fetchOne(MAPPER);
    }

    @Override
    public DomainRequestResponse insert(final DomainRequest request) {
        final DomainDetails details = request.getDomainDetails();
        final UUID detailsId = detailsRepository.insert(details);

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
        final Condition condition = idEquals(id).and(not(stausEquals(APPROVED)));
        return deleteRequestByCondition(condition);
    }

    @Override
    public boolean cancel(final Identifier identifier) {
        return deleteRequestByCondition(identifierEquals(identifier));
    }

    private boolean deleteRequestByCondition(final Condition condition) {
        final Optional<UUID> detailsId =
                context.delete(DOMAIN_REQUESTS.getTable())
                       .where(condition)
                       .returning(DOMAIN_REQUESTS.getField(DETAILS_ID))
                       .fetchOptional()
                       .map(result -> result.get(DOMAIN_REQUESTS.getField(DETAILS_ID)));

        return detailsId.map(detailsRepository::delete).orElse(false);
    }

    private SelectConditionStep<Record6<UUID, String, UUID, UUID, String, String>> selectByCondition(final Condition condition) {
        return context.select(DOMAIN_REQUESTS.getField(ID),
                              DOMAIN_REQUESTS.getField(STATUS),
                              DOMAIN_REQUESTS.getField(CLIENT_ID),
                              DOMAIN_DETAILS.getField(ID),
                              DOMAIN_DETAILS.getField(DOMAIN_NAME),
                              DOMAIN_DETAILS.getField(DESCRIPTION))
                      .from(DOMAIN_REQUESTS.getTable())
                      .join(DOMAIN_DETAILS.getTable()).on(DOMAIN_REQUESTS.getField(DETAILS_ID).equal(DOMAIN_DETAILS.getField(ID)))
                      .where(condition);

    }

    private static Condition stausEquals(DomainRequestStatus status) {
        return DOMAIN_REQUESTS.getField(STATUS).equal(inline(status.name()));
    }

    private static Condition idEquals(final UUID id) {
        return DOMAIN_REQUESTS.getField(ID).equal(inline(id));
    }

    private static Condition identifierEquals(final Identifier identifier) {
        return DOMAIN_REQUESTS.getField(ID).equal(inline(identifier.getRequestId()))
                              .and(DOMAIN_REQUESTS.getField(CLIENT_ID).equal(identifier.getClientId()));
    }
}
