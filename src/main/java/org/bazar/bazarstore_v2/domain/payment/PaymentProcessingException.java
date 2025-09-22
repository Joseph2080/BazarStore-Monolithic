package org.bazar.bazarstore_v2.domain.payment;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String message) {
        super(message);
    }
    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
