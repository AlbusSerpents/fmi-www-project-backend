package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.domain.DomainDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainRequest {

    private UUID id;
    private UUID clientId;

    @NotNull
    private DomainRequestStatus status;
    private DomainDetails domainDetails;

    @SuppressWarnings("WeakerAccess")
    public DomainRequest(final UUID clientId, final DomainRequestStatus status, final DomainDetails domainDetails) {
        this(null, clientId, status, domainDetails);
    }

    public enum DomainRequestStatus {
        SENT, APPROVED
    }
}
