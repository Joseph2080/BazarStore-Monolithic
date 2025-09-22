package org.bazar.bazarstore_v2.domain.productCatalog;


import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class ProductCatalogNotFoundException extends EntityNotFoundException {
    public ProductCatalogNotFoundException(String message) {
        super(message);
    }
}
