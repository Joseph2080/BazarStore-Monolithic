package org.bazar.bazarstore_v2.domain.cart;

import java.math.BigDecimal;

public class CartItem {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private Long storeId;

    public CartItem(Long productId, String productName, int quantity, BigDecimal price, Long storeId) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.storeId = storeId;
    }

    public CartItem() {

    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getStoreId() {return storeId;}

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
