package org.bazar.bazarstore_v2.domain.cart;

import java.util.List;

public class Cart {
    private String uuid;
    private List<CartItem> items;
    public Cart(String uuid, List<CartItem> items) {
        this.uuid = uuid;
        this.items = items;
    }

    public Cart() {

    }

    public String getUuid() {
        return uuid;
    }

    public List<CartItem> getItems() {
        return items;
    }
}
