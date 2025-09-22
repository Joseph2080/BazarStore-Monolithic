package org.bazar.bazarstore_v2.domain.store;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class StoreRequestDto {
    @NotEmpty(message = "name cannot be empty")
    @Size(max = 100, message = "name cannot exceed 100 characters")
    private String name;

    @NotEmpty(message = "description cannot be empty")
    @Size(max = 500, message = "description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "merchant id cannot be empty")
    @Positive(message = "merchant id must be a positive number")
    private Long merchantId;

}
