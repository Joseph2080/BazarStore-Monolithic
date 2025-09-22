package org.bazar.bazarstore_v2.domain.merchant;

import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;


@Component
public class MerchantDtoMapper implements DtoMapper<Merchant, MerchantRequestDto, MerchantResponseDto> {
    @Override
    public Merchant convertDtoToEntity(MerchantRequestDto merchantRequestDto) {
        return Merchant.builder()
                .taxId(merchantRequestDto.getTaxId())
                .firstname(merchantRequestDto.getFirstname())
                .middleName(merchantRequestDto.getMiddleName())
                .lastName(merchantRequestDto.getLastName())
                .cognitoUUID(merchantRequestDto.getUserId())
                .build();
    }

    @Override
    public MerchantResponseDto convertEntityToResponseDto(Merchant merchant) {
        return MerchantResponseDto.builder()
                .merchantId(merchant.getId())
                .taxId(merchant.getTaxId())
                .firstname(merchant.getFirstname())
                .middleName(merchant.getMiddleName())
                .lastName(merchant.getLastName())
                .cognitoUserId(merchant.getCognitoUUID())
                .build();
    }

    @Override
    public void updateEntityFromDto(MerchantRequestDto merchantRequestDto, Merchant merchant) {
        merchant.setFirstname(merchantRequestDto.getFirstname());
        merchant.setLastName(merchantRequestDto.getLastName());
        merchant.setMiddleName(merchantRequestDto.getMiddleName());
        merchant.setTaxId(merchantRequestDto.getTaxId());
    }
}
