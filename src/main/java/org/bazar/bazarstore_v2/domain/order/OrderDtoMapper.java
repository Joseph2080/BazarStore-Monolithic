package org.bazar.bazarstore_v2.domain.order;

import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper implements DtoMapper<Order, OrderRequestDto, OrderResponseDto> {

    @Override
    public Order convertDtoToEntity(OrderRequestDto orderRequestDto) {
        return Order.builder()
                .customerId(orderRequestDto.getCustomerId())
                .build();
    }

    @Override
    public OrderResponseDto convertEntityToResponseDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .totalAmount(order.calculateTotalCost())
                .build();
    }


    @Override
    public void updateEntityFromDto(OrderRequestDto orderRequestDto, Order order) {
        //need to update accordingly as well here....
    }
}
