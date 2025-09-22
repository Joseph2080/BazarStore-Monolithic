package org.bazar.bazarstore_v2.domain.merchant;


import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class MerchantNotFoundException extends EntityNotFoundException {
    public MerchantNotFoundException(String message) {
        super(message);
    }
}
