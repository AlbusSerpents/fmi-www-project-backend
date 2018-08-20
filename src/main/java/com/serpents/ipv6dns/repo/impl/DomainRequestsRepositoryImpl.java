package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.DomainDetailsRepository;
import com.serpents.ipv6dns.domain.request.DomainRequest;
import com.serpents.ipv6dns.domain.request.DomainRequest.DomainRequestStatus;
import com.serpents.ipv6dns.domain.request.DomainRequestInfo;
import com.serpents.ipv6dns.domain.request.DomainRequestsRepository;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.domain.request.DomainRequest.DomainRequestStatus.APPROVED;
import static com.serpents.ipv6dns.domain.request.DomainRequest.DomainRequestStatus.SENT;
import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqTable.*;
import static com.serpents.ipv6dns.utils.TimeUtils.nowAtUtc;
import static org.jooq.impl.DSL.*;

@Repository
public class DomainRequestsRepositoryImpl implements DomainRequestsRepository {

    private final RecordMapper<Record7<UUID, String, Integer, UUID, String, String, UUID>, DomainRequestInfo> MAPPER = record -> {
        final DomainDetails domainDetails = new DomainDetails(record.value4(), record.value5(), record.value6());
        return new DomainRequestInfo(record.value1(), record.value2(), record.value3(), domainDetails, record.value7());
    };

    private final DSLContext context;
    private final DomainDetailsRepository detailsRepository;

    @Autowired
    public DomainRequestsRepositoryImpl(final DSLContext context, final DomainDetailsRepository detailsRepository) {
        this.context = context;
        this.detailsRepository = detailsRepository;
    }

    @Override
    public List<DomainRequestInfo> findPending() {
        return selectByCondition(statusEquals(SENT)).fetch(MAPPER);
    }

    @Override
    public DomainRequestInfo findById(final UUID id) {
        return selectByCondition(idEquals(id)).fetchOne(MAPPER);
    }

    @Override
    public void insert(final DomainRequest request) {
        final DomainDetails details = request.getDomainDetails();
        final UUID detailsId = detailsRepository.insert(details);

        context.insertInto(DOMAIN_REQUESTS.getTable(), DOMAIN_REQUESTS.getField(STATUS), DOMAIN_REQUESTS.getField(CLIENT_ID), DOMAIN_REQUESTS.getField(DETAILS_ID))
               .values(inline(request.getStatus().name()), inline(request.getClientId()), inline(detailsId))
               .returning(DOMAIN_REQUESTS.getField(ID))
               .fetchOne()
               .get(DOMAIN_REQUESTS.getField(ID));
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
        final Condition condition = idEquals(id).and(not(statusEquals(APPROVED)));
        return deleteRequestByCondition(condition);
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

    private SelectConditionStep<Record7<UUID, String, Integer, UUID, String, String, UUID>> selectByCondition(final Condition condition) {
        return context.select(DOMAIN_REQUESTS.getField(ID),
                              CLIENT_USERS.getField(NAME),
                              CLIENT_USERS.getField(FACULTY_NUMBER),
                              DOMAIN_DETAILS.getField(ID),
                              DOMAIN_DETAILS.getField(DOMAIN_NAME),
                              DOMAIN_DETAILS.getField(DESCRIPTION),
                              DOMAIN_REQUESTS.getField(CLIENT_ID))
                      .from(DOMAIN_REQUESTS.getTable())
                      .join(DOMAIN_DETAILS.getTable()).on(DOMAIN_REQUESTS.getField(DETAILS_ID).equal(DOMAIN_DETAILS.getField(ID)))
                      .join(CLIENT_USERS.getTable()).on(DOMAIN_REQUESTS.getField(CLIENT_ID).equal(CLIENT_USERS.getField(ID)))
                      .where(condition);

    }

    private static Condition statusEquals(DomainRequestStatus status) {
        return DOMAIN_REQUESTS.getField(STATUS).equal(inline(status.name()));
    }

    private static Condition idEquals(final UUID id) {
        return DOMAIN_REQUESTS.getField(ID).equal(inline(id));
    }
}
