package org.bazar.bazarstore_v2.common.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bazar.bazarstore_v2.common.exception.InvalidFieldException;

import java.util.List;
import java.util.Set;

public final class ValidationUtil {
    private ValidationUtil() {}
    private static final String DEFAULT_NULL_ARGUMENT_MESSAGE = "argument cannot be null.";
    private static final String NULL_ARGUMENT_LIST_MESSAGE = "the argument list cannot be null.";
    private static final String NULL_ELEMENT_AT_INDEX_TEMPLATE = "argument at index [%d] can not be null";
    private static final String DEFAULT_INVALID_FIELD_MESSAGE = "invalid field value";


    public static void throwIfArgumentIsNull(Object object) {
        validateNotNull(object, DEFAULT_NULL_ARGUMENT_MESSAGE);
    }

    public static void throwIfArgumentListIsNull(List<Object> objectList) {
        validateNotNull(objectList, NULL_ARGUMENT_LIST_MESSAGE);
        for (int i = 0; i < objectList.size(); i++) {
            validateNotNull(objectList.get(i), String.format(NULL_ELEMENT_AT_INDEX_TEMPLATE, i));
        }
    }

    public static <T> void validateDtoFields(T dto) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .findFirst()
                        .orElse(DEFAULT_INVALID_FIELD_MESSAGE);
                throw new InvalidFieldException(message);
            }
        }
    }

    private static void validateNotNull(Object object, String errorMessage) {
        if (object == null) {
            throw new InvalidFieldException(errorMessage);
        }
    }
}
