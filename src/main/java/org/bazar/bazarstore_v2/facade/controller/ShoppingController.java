package org.bazar.bazarstore_v2.facade.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bazar.bazarstore_v2.domain.cart.CartDto;
import org.bazar.bazarstore_v2.facade.ShoppingExperienceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.buildResponse;

@Tag(name = "Shopping API", description = "API for managing user's shopping cart")
@RestController
@RequestMapping("/api/v1/shop")
public class ShoppingController {

    private final ShoppingExperienceFacade shoppingFacade;
    private static final String TEMP_UUID = "random123";

    @Autowired
    public ShoppingController(ShoppingExperienceFacade shoppingFacade) {
        this.shoppingFacade = shoppingFacade;
    }

    @Operation(summary = "Add product to cart", description = "Add a specific product to the authenticated user's cart.")
    @ApiResponse(responseCode = "200", description = "Product added to cart successfully")
    @PostMapping("/cart/item")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody CartDto cartDto, Principal principal) {
        assignUUIdToCartDto(cartDto, principal);
        shoppingFacade.addProductToCart(cartDto);
        return buildResponse(null, HttpStatus.OK, "Product added to cart successfully.");
    }

    @Operation(summary = "Create a new cart", description = "Creates a complete cart for the authenticated user.")
    @ApiResponse(responseCode = "201", description = "Cart created successfully")
    @PostMapping("/cart")
    public ResponseEntity<Map<String, Object>> createCart(@RequestBody CartDto cartDto, Principal principal) {
        assignUUIdToCartDto(cartDto, principal);
        shoppingFacade.createCart(cartDto);
        return buildResponse(null, HttpStatus.CREATED, "Cart created successfully.");
    }

    @Operation(summary = "Remove product from cart", description = "Removes a product from the authenticated user's cart.")
    @ApiResponse(responseCode = "200", description = "Product removed successfully")
    @DeleteMapping("/cart/item/{productId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long productId, Principal principal) {
        shoppingFacade.removeProductFromCart(TEMP_UUID, productId);
        return buildResponse(null, HttpStatus.OK, "Product removed from cart successfully.");
    }

    @Operation(summary = "Clear user's cart", description = "Clears all items from the authenticated user's cart.")
    @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    @DeleteMapping("/cart")
    public ResponseEntity<Map<String, Object>> clearCart(Principal principal) {
        shoppingFacade.clearCart(TEMP_UUID);
        return buildResponse(null, HttpStatus.OK, "Cart cleared successfully.");
    }

    private void assignUUIdToCartDto(CartDto cartDto, Principal principal){
        cartDto.setUuid(TEMP_UUID);
    }
}
