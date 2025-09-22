package org.bazar.bazarstore_v2.domain.product;

import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;


@Component
public class ProductDtoMapper implements DtoMapper<Product, ProductRequestDto, ProductResponseDto> {
    @Override
    public Product convertDtoToEntity(ProductRequestDto productRequestDto) {
        return Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .discount(productRequestDto.getDiscount())
                .stock(productRequestDto.getStock())
                .build();
    }

    @Override
    public ProductResponseDto convertEntityToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .stock(product.getStock())
                .storeId(product.getStore().getId())
                .build();
    }

    @Override
    public void updateEntityFromDto(ProductRequestDto productRequestDto, Product product) {
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setStock(productRequestDto.getStock());
    }
}
