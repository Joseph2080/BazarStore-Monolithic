package org.bazar.bazarstore_v2.domain.payment;

import org.bazar.bazarstore_v2.domain.order.Order;
import org.bazar.bazarstore_v2.domain.order.OrderResponseDto;

public interface PaymentService {
    PaymentResponse handlePaymentWithMethod(OrderResponseDto orderResponseDto, PaymentRequestDto paymentRequestDto);
    String generatePaymentLink(Order order);
}
