package com.serpents.ipv6dns.user.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.serpents.ipv6dns.credentials.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@NoArgsConstructor
public class ProfileUpdateRequest {
    @NotNull
    private Optional<UserRole> role;

    @NotBlank
    @Email
    private String email;

    private Optional<ChangePasswordRequest> passwordRequest;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        @JsonProperty(access = WRITE_ONLY)
        private String originalPassword;
        @JsonProperty(access = WRITE_ONLY)
        private String newPassword;
    }

}
