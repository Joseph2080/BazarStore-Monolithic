package org.bazar.bazarstore_v2.domain.productCatalog;

import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;


@Component
public class ProductCatalogDtoMapper implements DtoMapper<ProductCatalog, ProductCatalogRequestDto, ProductCatalogResponseDto> {
    @Override
    public ProductCatalog convertDtoToEntity(ProductCatalogRequestDto productCatalogRequestDto) {
        return ProductCatalog.builder()
                .build();
    }

    @Override
    public ProductCatalogResponseDto convertEntityToResponseDto(ProductCatalog productCatalog) {
        return ProductCatalogResponseDto
                .builder()
                .productCatalogId(productCatalog.getId())
                .build();
    }

    @Override
    public void updateEntityFromDto(ProductCatalogRequestDto productCatalogRequestDto, ProductCatalog productCatalog) {
        //do nothing for now since we are only updating external dependencies
    }

}
