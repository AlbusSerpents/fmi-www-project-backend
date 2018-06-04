package com.serpents.ipv6dns.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Data
@NoArgsConstructor
public class DomainRequestApproval {
    private Optional<String> ipAddress;

    @NotBlank
    @JsonProperty("ipAddress")
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ofNullable(ipAddress);
    }
}
