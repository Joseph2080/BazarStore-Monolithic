package org.bazar.bazarstore_v2.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotEmpty(message = "email can not be empty")
    @Email
    private String email;
    @NotEmpty(message = "phoneNumber can not be empty")
    private String phoneNumber;
    @NotEmpty(message = "password can not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
