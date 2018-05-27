package com.serpents.ipv6dns.user.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@NoArgsConstructor
public class RegistrationRequest {
    @NotBlank
    private String username;

    @NotBlank
    @JsonProperty(required = true, access = WRITE_ONLY)
    private String password;

    @NotBlank
    private String name;

    @NotNull
    @Min(10000)
    @Max(99999)
    private Integer facultyNumber;

    private String email;

}
