package org.bazar.bazarstore_v2.domain.cart;

import java.util.List;

public interface CartService {
    public void create(String cartId, Cart cart);
    void clearById(String cartId);
    Cart findById(String cartId);
    Cart findCartByCustomerId(String customerId);
    void removeById(String cartId);
    void removeByCustomerId(String uuid);
    void removeItem(String cartId, Long productId);
    void removeItemListById(String uuid, List<Long> productIdList);
    void addItem(String cartId, CartItem cartItem);
    Integer countTotalProductsInCarts(Long productId);
}
