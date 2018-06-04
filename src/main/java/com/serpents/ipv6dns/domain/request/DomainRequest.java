package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.domain.DomainDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DomainRequest {

    private UUID id;
    @NotNull
    private DomainRequestStatus status;
    private UUID clientId;

    private DomainDetails domainDetails;

}
