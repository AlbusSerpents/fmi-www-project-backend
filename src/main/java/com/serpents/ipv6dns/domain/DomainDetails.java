package com.serpents.ipv6dns.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Data
@NoArgsConstructor
public class DomainDetails {
    private UUID id;

    private String domainName;
    private Optional<String> description = empty();

    @JsonProperty(value = "description")
    public void setDescription(final String description) {
        this.description = ofNullable(description);
    }
}
