package org.bazar.bazarstore_v2.domain.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequestDto {
    @NotEmpty(message = "name cannot be empty")
    @Size(max = 100, message = "name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "price cannot be empty")
    @Positive(message = "price must be a positive number")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero(message = "discount must be a positive number or zero")
    private Integer discount;

    @NotNull(message = "stock cannot be empty")
    @Min(value = 0, message = "stock cannot be negative")
    private Integer stock;

    @NotNull(message = "store id cannot be empty")
    @Positive(message = "store id must be a positive number")
    private Long storeId;
}
