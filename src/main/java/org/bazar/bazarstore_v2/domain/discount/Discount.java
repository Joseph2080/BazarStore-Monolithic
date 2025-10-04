package org.bazar.bazarstore_v2.domain.discount;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;
import org.bazar.bazarstore_v2.domain.product.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Discount extends BaseJpaEntity {
    private String code;
    private BigDecimal percentage; // e.g., 0.15 for 15% off
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "discount_products",
            joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> applicableProducts;

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return active && (validFrom == null || !now.isBefore(validFrom)) && (validUntil == null || !now.isAfter(validUntil));
    }
}
