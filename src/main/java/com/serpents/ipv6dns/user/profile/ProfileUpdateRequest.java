package com.serpents.ipv6dns.user.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.serpents.ipv6dns.credentials.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.serpents.ipv6dns.credentials.UserRole.fromToken;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Data
public class ProfileUpdateRequest {

    @NotBlank
    @Email
    private String email;

    private Optional<UserRole> role;
    private Optional<ChangePasswordRequest> passwordRequest;

    public ProfileUpdateRequest() {
        role = empty();
        email = null;
        passwordRequest = empty();
    }

    @JsonProperty("role")
    public void setRole(char roleToken) {
        this.role = fromToken(roleToken);
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
