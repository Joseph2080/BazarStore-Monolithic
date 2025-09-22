package org.bazar.bazarstore_v2.domain.merchant;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MerchantRequestDto {

    @NotEmpty(message = "tax ID cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "Tax ID must be alphanumeric and between 8-15 characters")
    private String taxId;

    @NotEmpty(message = "first name cannot be empty")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstname;

    @Size(max = 50, message = "middle name cannot exceed 50 characters")
    private String middleName;

    @NotEmpty(message = "last name cannot be empty")
    @Size(max = 50, message = "last name cannot exceed 50 characters")
    private String lastName;

    @NotEmpty(message = "user ID cannot be empty")
    private String userId;

}
