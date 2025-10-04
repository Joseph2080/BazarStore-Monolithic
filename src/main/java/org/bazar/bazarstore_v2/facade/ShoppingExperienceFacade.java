package org.bazar.bazarstore_v2.facade;

import org.bazar.bazarstore_v2.common.exception.InvalidFieldException;
import org.bazar.bazarstore_v2.domain.cart.Cart;
import org.bazar.bazarstore_v2.domain.cart.CartDto;
import org.bazar.bazarstore_v2.domain.cart.CartItem;
import org.bazar.bazarstore_v2.domain.cart.CartService;
import org.bazar.bazarstore_v2.domain.cart.CartUtil;
import org.bazar.bazarstore_v2.domain.discount.DiscountResponseDto;
import org.bazar.bazarstore_v2.domain.discount.DiscountService;
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

    private final DiscountService discountService;

    @Autowired
    public ShoppingExperienceFacade(ProductService productService,
                                    CartService cartService,
                                    DiscountService discountService) {
        this.productService = productService;
        this.cartService = cartService;
        this.discountService = discountService;
    }

    public void addProductToCart(CartDto cartDto) {
        validateCartDto(cartDto);
        ProductResponseDto product = validateProductAvailability(cartDto);
        String cartKey = CartUtil.generateCartKey(cartDto.getUuid());
        Cart existingCart = cartService.findById(cartKey);
        if (existingCart == null) {
            Cart newCart = new Cart(cartDto.getUuid(), List.of(createCartItem(product, cartDto.getQuantity())));
            cartService.create(cartKey, newCart);
        }
        List<CartItem> items = existingCart.getItems();
        existingCart
                .getItems()
                .stream()
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
                product.getStoreId(),
                null
        );
    }

    public void applyDiscountByCode(String uuid, String code) {
        updateDiscount(uuid, code, true);
    }

    public void removeDiscountByCode(String uuid, String code) {
        updateDiscount(uuid, code, false);
    }

    private void updateDiscount(String uuid, String code, boolean apply) {
        Cart cart = cartService.findById(CartUtil.generateCartKey(uuid));
        if (cart == null) return; // nothing to do if no cart
        DiscountResponseDto discount = discountService.findDiscountByCode(code);
        if (discount == null) return; // ignore invalid discount code
        for (CartItem item : cart.getItems()) {
            if (discount.getProductIdSet().contains(item.getProductId())) {
                item.setDiscountId(apply ? discount.getId() : null);
            }
        }
        cartService.create(CartUtil.generateCartKey(uuid), cart);
    }

}
