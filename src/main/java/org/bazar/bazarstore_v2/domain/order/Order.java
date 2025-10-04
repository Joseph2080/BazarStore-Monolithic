package org.bazar.bazarstore_v2.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order extends BaseJpaEntity {
    private String customerId;
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrderItem> items = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    private String paymentId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public BigDecimal calculateTotalCost() {
        return items.stream()
                .map(orderItem -> {
                    BigDecimal price = orderItem.getProduct().getPrice();
                    if (orderItem.getDiscount() != null && orderItem.getDiscount().isActive()) {
                        BigDecimal discountPercent = orderItem.getDiscount().getPercentage(); // BigDecimal like 0.10 for 10%
                        price = calculateDiscountedPrice(price, discountPercent);
                    }
                    return price.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, BigDecimal discountPercentage) {
        BigDecimal validDiscount = discountPercentage.min(BigDecimal.ONE).max(BigDecimal.ZERO);
        BigDecimal discounted = originalPrice.subtract(originalPrice.multiply(validDiscount));
        return discounted.max(BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", items=" + items +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
