package org.bazar.bazarstore_v2.common.util;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.exception.EntityReferenceException;
import org.bazar.bazarstore_v2.common.exception.InvalidFieldException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public final class RepositoryUtil {
    private RepositoryUtil() {}
    private static final String entityNotFoundMessage = "unable to delete at this moment as its referenced by existing child entity";

    public static <E, ID> Page<E> getPage(JpaRepository<E, ID> repository, Pageable pageable) {
        if (pageable.getPageNumber() < 0) throw new InvalidFieldException("Page index must be non-negative.");
        return repository.findAll(pageable);
    }

    public static <E, ID> E findEntityByIdOrElseThrow(JpaRepository<E, ID> repository, ID id, EntityNotFoundException e) {
        return repository.findById(id).orElseThrow(() -> (e));
    }

    public static <E, ID> void deleteById(JpaRepository<E, ID> repository,
                                          ID id,
                                          EntityNotFoundException entityNotFoundException) {
        try {
            repository.delete(findEntityByIdOrElseThrow(repository, id, entityNotFoundException));
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new EntityReferenceException(entityNotFoundMessage, dataIntegrityViolationException);
        }
    }

    public static <E,ID> void deleteAll(JpaRepository<E, ID> repository) {
        try {
            repository.deleteAll();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new EntityReferenceException(entityNotFoundMessage, dataIntegrityViolationException);
        }
    }

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }
}
