package org.bazar.bazarstore_v2.domain.order;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
