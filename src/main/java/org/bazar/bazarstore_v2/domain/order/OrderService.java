package org.bazar.bazarstore_v2.domain.order;


import org.bazar.bazarstore_v2.common.service.Service;

public interface OrderService extends Service<Order, Long, OrderRequestDto, OrderResponseDto> {
    //need to refactor to use an enum instead
    void updateOrderStatusByOrderId(Long id, String status);
}
