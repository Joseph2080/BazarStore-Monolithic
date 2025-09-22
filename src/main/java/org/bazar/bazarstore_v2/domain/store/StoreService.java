package org.bazar.bazarstore_v2.domain.store;

import org.bazar.bazarstore_v2.common.service.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreService extends Service<Store, Long, StoreRequestDto, StoreResponseDto> {
    void updateWallpaper(Long storeId, MultipartFile storeWallpaper);
    void updateLogo(Long storeId, MultipartFile storeWallpaper);
    @Cacheable(value = "storesCache", key = "#merchantId", cacheManager = "redisCacheManager")
    List<StoreResponseDto> findAllStoresByMerchantId(Long merchantId);
    String generateWallpaperPresignedUrl(Long storeId);
    String generateLogoPresignedUrl(Long storeId);
}
