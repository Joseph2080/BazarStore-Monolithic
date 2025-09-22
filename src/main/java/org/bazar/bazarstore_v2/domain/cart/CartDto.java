package org.bazar.bazarstore_v2.domain.cart;

public class CartDto {
    private String uuid;
    private Long productId;
    private Integer quantity;

    public CartDto(String uuid, Long productId, Integer quantity) {
        this.uuid = uuid;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartDto() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
