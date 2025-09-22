package org.bazar.bazarstore_v2.facade;

import org.bazar.bazarstore_v2.common.exception.InvalidFieldException;
import org.bazar.bazarstore_v2.domain.cart.Cart;
import org.bazar.bazarstore_v2.domain.cart.CartDto;
import org.bazar.bazarstore_v2.domain.cart.CartItem;
import org.bazar.bazarstore_v2.domain.cart.CartService;
import org.bazar.bazarstore_v2.domain.cart.CartUtil;
import org.bazar.bazarstore_v2.domain.product.ProductResponseDto;
import org.bazar.bazarstore_v2.domain.product.ProductService;
import org.bazar.bazarstore_v2.facade.exception.InsufficientStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ShoppingExperienceFacade {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ShoppingExperienceFacade(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    public void addProductToCart(CartDto cartDto) {
        validateCartDto(cartDto);
        ProductResponseDto product = validateProductAvailability(cartDto);
        String cartKey = CartUtil.generateCartKey(cartDto.getUuid());
        Cart existingCart = (Cart) cartService.findById(cartKey); // Assumes this getter method exists
        if (existingCart == null) {
            Cart newCart = new Cart(cartDto.getUuid(),  List.of(createCartItem(product, cartDto.getQuantity())));
            cartService.create(cartKey, newCart);
        }
        List<CartItem> items = existingCart.getItems();
        items.stream()
                .filter(item -> item.getProductId().equals(product.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> {
                            if (existingItem.getQuantity() != cartDto.getQuantity()) {
                                existingItem.setQuantity(cartDto.getQuantity());
                                cartService.create(cartKey, existingCart);
                            }
                        },
                        () -> {
                            items.add(createCartItem(product, cartDto.getQuantity()));
                            cartService.create(cartKey, existingCart);
                        }
                );
    }

    public void createCart(CartDto cartDto) {
        validateCartDto(cartDto);
        String uuid = cartDto.getUuid();
        ProductResponseDto product = validateProductAvailability(cartDto);
        Cart cart = new Cart(uuid, List.of(createCartItem(product, cartDto.getQuantity())));
        cartService.create(CartUtil.generateCartKey(uuid), cart);
    }

    public void removeProductFromCart(String uuid, Long productId) {
        cartService.removeItem(CartUtil.generateCartKey(uuid), productId);
    }

    public void clearCart(String uuid) {
        cartService.clearById(CartUtil.generateCartKey(uuid));
    }

    private void validateCartDto(CartDto cartDto) {
        if (cartDto == null || cartDto.getUuid() == null || cartDto.getProductId() == null || cartDto.getQuantity() == null) {
            throw new InvalidFieldException("Cart must include uuid, productId, and quantity.");
        }
    }

    private ProductResponseDto validateProductAvailability(CartDto cartDto) {
        Long productId = cartDto.getProductId();
        int quantity = cartDto.getQuantity();

        if (quantity <= 0) {
            throw new InvalidFieldException("Quantity must be greater than 0.");
        }

        ProductResponseDto product = productService.findByIdOrElseThrowException(productId);

        if (!productService.isProductAvailable(productId, quantity)) {
            throw new InsufficientStockException("Product " + product.getName() + " does not have enough stock.");
        }

        int alreadyReserved = cartService.countTotalProductsInCarts(productId);
        int availableStock = product.getStock() - alreadyReserved;

        if (availableStock < quantity) {
            throw new InsufficientStockException("Product " + product.getName() + " is understocked. " +
                    "Available: " + availableStock + ", Requested: " + quantity);
        }
        return product;
    }

    private CartItem createCartItem(ProductResponseDto product, int quantity) {
        return new CartItem(
                product.getProductId(),
                product.getName(),
                quantity,
                product.getPrice(),
                product.getStoreId()
        );
    }

}
