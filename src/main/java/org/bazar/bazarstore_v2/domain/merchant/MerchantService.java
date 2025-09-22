package org.bazar.bazarstore_v2.domain.merchant;

import org.bazar.bazarstore_v2.common.service.Service;
import org.springframework.cache.annotation.Cacheable;

public interface MerchantService extends Service<Merchant, Long, MerchantRequestDto, MerchantResponseDto> {
    @Cacheable(value = "merchant", key = "#id", cacheManager = "redisCacheManager")
    MerchantResponseDto findMerchantResponseDtoByCognitoUserId(String cognitoUserId);
}
