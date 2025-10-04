package org.bazar.bazarstore_v2.domain.order;


import org.bazar.bazarstore_v2.common.service.Service;
import org.bazar.bazarstore_v2.domain.payment.PaymentResponse;

public interface OrderService extends Service<Order, Long, OrderRequestDto, OrderResponseDto> {
    //need to refactor to use an enum instead
    OrderStatus updateOrderStatusByOrderId(Long id, PaymentResponse paymentResponse);
}
