package org.bazar.bazarstore_v2.domain.payment;

public class PaymentResponse {

    private String paymentId;
    private String status;

    public PaymentResponse(String paymentId, String status) {
        this.paymentId = paymentId;
        this.status = status;
    }

    public PaymentResponse() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "paymentId='" + paymentId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
