package org.bazar.bazarstore_v2.domain.payment;

public class PaymentRequestDto {
    private String paymentMethodId;
    private String notes;
    public PaymentRequestDto(String paymentMethodId, String notes) {
        this.paymentMethodId = paymentMethodId;
        this.notes = notes;
    }
    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getNotes() {
        return notes;
    }
}
