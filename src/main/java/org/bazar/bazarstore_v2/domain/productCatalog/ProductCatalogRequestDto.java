package org.bazar.bazarstore_v2.domain.productCatalog;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCatalogRequestDto {
    @NotNull(message = "product id cannot be empty")
    @Positive(message = "product id must be a positive number")
    private Long productId;
    @NotNull(message = "image file cannot be empty")
    private MultipartFile imageFile;
}
