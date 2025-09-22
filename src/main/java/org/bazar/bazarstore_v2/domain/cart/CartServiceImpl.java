package org.bazar.bazarstore_v2.domain.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final RedisTemplate<String, Cart> redisTemplate;

    @Autowired
    public CartServiceImpl(RedisTemplate<String, Cart> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void create(String cartId, Cart cart) {
        logger.debug("Creating/updating cart with id: {}", cartId);
        redisTemplate.opsForValue().set(cartId, cart);
    }

    @Override
    public void clearById(String cartId) {
        logger.info("Clearing cart by ID: {}", cartId);
        redisTemplate.delete(cartId);
    }

    @Override
    public Cart findById(String cartId) {
        logger.debug("Retrieving cart with id: {}", cartId);
        return Optional.of(redisTemplate.opsForValue().get(cartId))
                .orElseThrow(() -> new CartNotFoundException("Cart with id " + cartId + " not found"));
    }

    @Override
    public Cart findCartByCustomerId(String customerId) {
        String cartKey = CartUtil.generateCartKey(customerId);
        logger.debug("Finding cart for customerId {} using key: {}", customerId, cartKey);
        return findById(cartKey);
    }

    @Override
    public void removeById(String cartId) {
        logger.info("Removing cart by ID: {}", cartId);
        redisTemplate.delete(cartId);
    }

    @Override
    public void removeByCustomerId(String customerId) {
        String cartKey = CartUtil.generateCartKey(customerId);
        logger.info("Removing cart for customerId {} using key: {}", customerId, cartKey);
        redisTemplate.delete(cartKey);
    }

    @Override
    public void removeItem(String cartId, Long productId) {
        Cart cart = findById(cartId);
        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        if (removed) {
            logger.debug("Removed productId {} from cartId {}", productId, cartId);
            create(cartId, cart);
        } else {
            logger.debug("ProductId {} not found in cartId {}", productId, cartId);
        }
    }

    @Override
    public void removeItemListById(String cartId, List<Long> productIdList) {
        Cart cart = findById(cartId);
        boolean removed = cart.getItems().removeIf(item -> productIdList.contains(item.getProductId()));
        if (removed) {
            logger.debug("Removed products {} from cartId {}", productIdList, cartId);
            create(cartId, cart);
        } else {
            logger.debug("None of the specified products were found in cartId {}", cartId);
        }
    }

    @Override
    public void addItem(String cartId, CartItem newItem) {
        Cart cart = findById(cartId);
        cart.getItems().removeIf(item -> item.getProductId().equals(newItem.getProductId()));
        cart.getItems().add(newItem);
        logger.debug("Added/Updated item {} to cartId {}", newItem, cartId);
        create(cartId, cart);
    }

    @Override
    public Integer countTotalProductsInCarts(Long productId) {
        int total = 0;
        Set<String> keys = redisTemplate.keys("cart:*");

        if (keys.isEmpty()) {
            logger.debug("No carts found for counting product: {}", productId);
            return total;
        }

        for (String key : keys) {
            Cart cart = redisTemplate.opsForValue().get(key);
            if (cart.getItems() == null) continue;
            int productCount = cart.getItems().stream()
                    .filter(item -> Objects.equals(item.getProductId(), productId))
                    .mapToInt(CartItem::getQuantity)
                    .sum();
            total += productCount;
        }

        logger.info("Total quantity of productId {} across all carts: {}", productId, total);
        return total;
    }
}
