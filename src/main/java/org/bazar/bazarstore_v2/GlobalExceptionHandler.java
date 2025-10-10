package org.bazar.bazarstore_v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.exception.EntityReferenceException;
import org.bazar.bazarstore_v2.common.exception.InvalidFieldException;
import org.bazar.bazarstore_v2.facade.exception.InsufficientStockException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.ConnectException;
import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.handleException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Operation(summary = "Handle entity not found", description = "Handles EntityNotFoundException when an entity is not found")
    @ApiResponse(responseCode = "404", description = "Entity not found")
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        return handleException(ex,
                HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Handle bad request", description = "Handles invalid field or argument exceptions")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ExceptionHandler({IllegalArgumentException.class, InvalidFieldException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Handle validation exceptions", description = "Handles validation exceptions for invalid fields")
    @ApiResponse(responseCode = "400", description = "Validation error, bad request")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return handleException(new InvalidFieldException(message),
                HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Handle database connection errors", description = "Handles database connection issues")
    @ApiResponse(responseCode = "500", description = "Internal server error, database connection issue")
    @ExceptionHandler({ConnectException.class, JDBCConnectionException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleDatabaseConnection(Exception ex) {
        return handleException(new RuntimeException("Unable to process request at the moment. Please try again later."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Handle entity reference conflict", description = "Handles conflicts when an entity is referenced")
    @ApiResponse(responseCode = "409", description = "Entity reference conflict")
    @ExceptionHandler(EntityReferenceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleEntityReference(EntityReferenceException ex) {
        return handleException(ex, HttpStatus.CONFLICT);
    }

    @Operation(summary = "Handle stock issues")
    @ApiResponse(responseCode = "400", description = "Insufficient stock")
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleInsufficientStock(InsufficientStockException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }
}
