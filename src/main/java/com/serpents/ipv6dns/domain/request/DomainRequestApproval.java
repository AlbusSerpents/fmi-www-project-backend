package com.serpents.ipv6dns.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Data
@NoArgsConstructor
public class DomainRequestApproval {
    private Optional<UUID> addressId;

    @NotBlank
    @JsonProperty("ipAddress")
    public void setIpAddress(final UUID addressId) {
        this.addressId = ofNullable(addressId);
    }
}
