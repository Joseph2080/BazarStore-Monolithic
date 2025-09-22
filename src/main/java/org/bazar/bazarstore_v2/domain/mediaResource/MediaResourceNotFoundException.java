package org.bazar.bazarstore_v2.domain.mediaResource;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;

public class MediaResourceNotFoundException extends EntityNotFoundException {
    public MediaResourceNotFoundException(String message) {
        super(message);
    }
}
