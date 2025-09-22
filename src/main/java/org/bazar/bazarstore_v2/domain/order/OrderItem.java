package org.bazar.bazarstore_v2.domain.order;


import java.math.BigDecimal;

public class OrderItem {
    //need to store store-id here instead as well
    private Long productId;
    private String productName;
    private int quantity;
    BigDecimal price;
    private Long storeId;

    // Constructors
    public OrderItem() {
    }

    public OrderItem(Long productId, String productName, int quantity, BigDecimal price, Long storeId) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.storeId = storeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getStoreId() {return storeId;}

    public void setStoreId(Long storeId) {this.storeId = storeId;}
}
