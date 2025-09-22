package org.bazar.bazarstore_v2.domain.product;


import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
