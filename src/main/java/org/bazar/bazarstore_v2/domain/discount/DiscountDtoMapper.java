package org.bazar.bazarstore_v2.domain.discount;

import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;
import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DiscountDtoMapper implements DtoMapper<Discount, DiscountRequestDto, DiscountResponseDto> {
    @Override
    public Discount convertDtoToEntity(DiscountRequestDto discountRequestDto) {
        return Discount.builder()
                .code("Generate random shareable code ")
                .validUntil(discountRequestDto.getValidUntil())
                .validFrom(discountRequestDto.getValidFrom())
                .active(false)
                .percentage(discountRequestDto.getPercentage())
                .build();
    }

    @Override
    public DiscountResponseDto convertEntityToResponseDto(Discount discount) {
        return DiscountResponseDto.builder()
                .id(discount.getId())
                .isValid(discount.isActive())
                .productIdSet(discount.getApplicableProducts()
                        .stream()
                        .map(BaseJpaEntity::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void updateEntityFromDto(DiscountRequestDto discountRequestDto, Discount discount) {

    }
}
