package org.bazar.bazarstore_v2.domain.store;


import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class StoreNotFoundException extends EntityNotFoundException {
    public StoreNotFoundException(String message) {
        super(message);
    }
}
