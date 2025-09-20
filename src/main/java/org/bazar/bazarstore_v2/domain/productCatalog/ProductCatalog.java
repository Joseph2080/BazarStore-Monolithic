package org.bazar.bazarstore_v2.domain.productCatalog;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseEntity;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResource;
import org.bazar.bazarstore_v2.domain.product.Product;

@Entity
@Builder
@Table(name = "product_catalog")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCatalog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_resource_id")
    private MediaResource resource;
}
