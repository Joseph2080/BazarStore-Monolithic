package org.bazar.bazarstore_v2.domain.discount;

import org.bazar.bazarstore_v2.common.service.Service;

public interface DiscountService extends Service<Discount, Long, DiscountRequestDto, DiscountResponseDto> {
    DiscountResponseDto findDiscountByCode(String code);
}
