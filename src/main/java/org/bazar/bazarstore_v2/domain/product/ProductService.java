package org.bazar.bazarstore_v2.domain.product;

import org.bazar.bazarstore_v2.common.service.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ProductService extends Service<Product, Long, ProductRequestDto, ProductResponseDto> {
    @Cacheable(value = "productList", key = "#storeId", cacheManager = "redisCacheManager")
    List<ProductResponseDto> findProductsByStoreId(Long storeId);
    boolean isProductAvailable(Long productId, int requestedQuantity);
    void updateProductStock(Long productId, int quantity);
}
