package org.bazar.bazarstore_v2.domain.product;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.bazar.bazarstore_v2.domain.store.Store;
import org.bazar.bazarstore_v2.domain.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.bazar.bazarstore_v2.common.util.ServiceConstants.PRODUCT_NOT_FOUND_MESSAGE;

@Service
public class ProductServiceImpl extends AbstractJpaService<
        Product,
        Long,
        ProductRequestDto,
        ProductResponseDto,
        ProductRepository> implements ProductService {

    private final StoreService storeService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductDtoMapper productDtoMapper,
                              StoreService storeService) {
        super(productRepository, productDtoMapper);
        this.storeService = storeService;
    }

    @Override
    protected void setEntityDependencies(Product product, ProductRequestDto productRequestDto) {
        logger.debug("[{}.setExternalDependencies] Setting store for product with storeId={}",
                getClass().getSimpleName(), productRequestDto.getStoreId());
        Store store = storeService.findEntityByIdOrElseThrowException(productRequestDto.getStoreId());
        product.setStore(store);
        logger.debug("[{}.setExternalDependencies] Store set successfully for product", getClass().getSimpleName());
    }

    @Override
    public List<ProductResponseDto> findProductsByStoreId(Long storeId) {
        logger.info("[{}.findProductsByStoreId] Fetching products for storeId={}", getClass().getSimpleName(), storeId);
        Optional<List<Product>> optionalProductsByStoreId = repository.findProductsByStoreId(storeId);
        List<ProductResponseDto> productResponseDtoList = optionalProductsByStoreId
                .map(this::transformEntityToDtoList)
                .orElse(null);
        logger.debug("[{}.findProductsByStoreId] Found {} products for storeId={}",
                getClass().getSimpleName(),
                productResponseDtoList != null ? productResponseDtoList.size() : 0,
                storeId);
        return productResponseDtoList;
    }

    @Override
    public boolean isProductAvailable(Long productId, int requestedQuantity) {
        logger.info("[{}.isProductAvailable] Checking availability for productId={} requestedQty={}", getClass().getSimpleName(), productId, requestedQuantity);
        boolean available = (findEntityByIdOrElseThrowException(productId).getStock() - requestedQuantity) > 0;
        logger.debug("[{}.isProductAvailable] Availability for productId={} is {}", getClass().getSimpleName(), productId, available);
        return available;
    }

    @Override
    public void updateProductStock(Long productId, int quantity) {
        logger.info("[{}.updateProductStock] Updating stock for productId={} by quantity={}", getClass().getSimpleName(), productId, quantity);
        Product product = findEntityByIdOrElseThrowException(productId);
        product.setStock(product.getStock() - quantity);
        repository.save(product);
        logger.debug("[{}.updateProductStock] Stock updated, new stock={}", getClass().getSimpleName(), product.getStock());
    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE);
    }
}
