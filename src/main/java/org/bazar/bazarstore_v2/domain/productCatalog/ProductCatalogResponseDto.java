package org.bazar.bazarstore_v2.domain.productCatalog;

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
public class ProductCatalogResponseDto {
    private long productCatalogId;
    private String productImageUrl;
}
