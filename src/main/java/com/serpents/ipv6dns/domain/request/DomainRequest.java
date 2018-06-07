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
    @NotNull
    private DomainRequestStatus status;
    private UUID clientId;

    private DomainDetails domainDetails;

    public DomainRequest(final UUID clientId, final DomainRequestStatus status, final DomainDetails domainDetails) {
        this.clientId = clientId;
        this.status = status;
        this.domainDetails = domainDetails;
    }

}
