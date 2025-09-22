package org.bazar.bazarstore_v2.domain.productCatalog;

import org.bazar.bazarstore_v2.common.service.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.Set;


public interface ProductCatalogService extends Service<ProductCatalog, Long, ProductCatalogRequestDto, ProductCatalogResponseDto> {
    @Cacheable(value = "productCatalogsCache", key = "#productId", cacheManager = "redisCacheManager")
    Set<ProductCatalogResponseDto> findProductCatalogsByProductId(long productId);
}
