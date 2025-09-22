package org.bazar.bazarstore_v2.domain.cart;


import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class CartNotFoundException extends EntityNotFoundException {

    public CartNotFoundException(String message) {super(message);}

    public CartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
