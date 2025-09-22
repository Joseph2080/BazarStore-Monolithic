package org.bazar.bazarstore_v2.facade;

import org.bazar.bazarstore_v2.domain.cart.Cart;
import org.bazar.bazarstore_v2.domain.cart.CartItem;
import org.bazar.bazarstore_v2.domain.order.OrderItem;
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

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::convertCartItemToOrderItem)
                .collect(Collectors.toList());
        /*return new Order(
                cart.getUuid(),
                orderItems,
                null,
                null
        );*/
        return null;
    }

    private OrderItem convertCartItemToOrderItem(CartItem cartItem) {
        return new OrderItem(
                cartItem.getProductId(),
                cartItem.getProductName(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartItem.getStoreId()
        );
    }

}
