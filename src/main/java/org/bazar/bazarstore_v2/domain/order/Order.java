package org.bazar.bazarstore_v2.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseEntity;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order extends BaseEntity {
    private String customerId;
    private List<OrderItem> items;
    private String status;
    private String paymentId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public BigDecimal calculateTotalCost() {
        return items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
    enum OrderStatus {
        SUCCESS,
        FAILURE,
        PENDING
    }
}
