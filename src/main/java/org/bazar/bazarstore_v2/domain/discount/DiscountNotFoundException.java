package org.bazar.bazarstore_v2.domain.discount;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class DiscountNotFoundException extends EntityNotFoundException {
    public DiscountNotFoundException(String message) {
        super(message);
    }

    public DiscountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
