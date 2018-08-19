package com.serpents.ipv6dns.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serpents.ipv6dns.domain.DomainDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DomainRequestInfo {
    private UUID id;
    private String clientName;
    private Integer facultyNumber;
    private DomainDetails details;

    @JsonIgnore
    private UUID clientId;

}
