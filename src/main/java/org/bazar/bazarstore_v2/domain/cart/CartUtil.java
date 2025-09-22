package org.bazar.bazarstore_v2.domain.cart;

public class CartUtil {
    private static final String CART_KEY_PREFIX = "cart:";

    private CartUtil(){}

    public static String generateCartKey(String uuid) {
        return CART_KEY_PREFIX + uuid;
    }
}
