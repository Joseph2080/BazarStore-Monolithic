package org.bazar.bazarstore_v2.facade;

import org.bazar.bazarstore_v2.domain.cart.Cart;
import org.bazar.bazarstore_v2.domain.cart.CartItem;
import org.bazar.bazarstore_v2.domain.discount.DiscountResponseDto;
import org.bazar.bazarstore_v2.domain.order.OrderItemDto;
import org.bazar.bazarstore_v2.domain.order.OrderRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartToOrderRequestAdapter {

    public OrderRequestDto adapt(Cart cart) {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or null");
        }

        List<OrderItemDto> orderItems = cart.getItems().stream()
                .map(this::convertCartItemToOrderItem)
                .collect(Collectors.toList());
        return OrderRequestDto.builder()
                .customerId(cart.getUuid())
                .items(orderItems)
                .build();
    }

    private OrderItemDto convertCartItemToOrderItem(CartItem cartItem) {
        return OrderItemDto
                .builder()
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .quantity(cartItem.getQuantity())
                .discountId(cartItem.getDiscountId())
                .build();
    }
}
