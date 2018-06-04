package com.serpents.ipv6dns.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.serpents.ipv6dns.domain.DomainDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DomainRequestResponse {
    private UUID id;
    private Optional<DomainDetails> details;

    @JsonProperty(value = "details")
    public DomainDetails getDetails() {
        return details.orElse(null);
    }
}
