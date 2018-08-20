package com.serpents.ipv6dns.user.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Data
public class ProfileUpdateRequest {

    @NotBlank
    @Email
    private String email;

    private Optional<ChangePasswordRequest> passwordRequest;

    public ProfileUpdateRequest() {
        email = null;
        passwordRequest = empty();
    }

    @JsonProperty(value = "passwordRequest")
    public void setPasswordRequest(final ChangePasswordRequest request) {
        this.passwordRequest = ofNullable(request);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        @NotBlank
        @JsonProperty(access = WRITE_ONLY)
        private String originalPassword;
        @NotBlank
        @JsonProperty(access = WRITE_ONLY)
        private String newPassword;
    }

}
