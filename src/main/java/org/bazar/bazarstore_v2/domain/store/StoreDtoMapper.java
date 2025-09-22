package org.bazar.bazarstore_v2.domain.store;

import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class StoreDtoMapper implements DtoMapper<Store, StoreRequestDto, StoreResponseDto> {
    @Override
    public Store convertDtoToEntity(StoreRequestDto storeRequestDto) {
        return Store.builder()
                .name(storeRequestDto.getName())
                .description(storeRequestDto.getDescription())
                .build();
    }

    @Override
    public StoreResponseDto convertEntityToResponseDto(Store store) {
        return StoreResponseDto.builder()
                .storeId(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .merchantId(store.getMerchant().getId())
                .build();
    }

    @Override
    public void updateEntityFromDto(StoreRequestDto storeRequestDto, Store store) {
        store.setName(storeRequestDto.getName());
        store.setDescription(storeRequestDto.getDescription());
    }
}
