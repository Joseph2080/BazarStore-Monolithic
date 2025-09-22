package org.bazar.bazarstore_v2.domain.order;

public class OrderItemDto {
    private String productId;
    private String productName;
    private int quantity;

    // Default constructor
    public OrderItemDto(String productId, String productName, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
    // Getters, setters, constructors
}
