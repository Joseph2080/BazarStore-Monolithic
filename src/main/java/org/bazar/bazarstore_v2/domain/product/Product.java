package org.bazar.bazarstore_v2.domain.product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;
import org.bazar.bazarstore_v2.domain.productCatalog.ProductCatalog;
import org.bazar.bazarstore_v2.domain.store.Store;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product extends BaseJpaEntity {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer discount;// create a new table and service to link discount instead
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCatalog> productCatalogs;

}
