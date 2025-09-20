package org.bazar.bazarstore_v2.domain.store;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseEntity;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResource;
import org.bazar.bazarstore_v2.domain.merchant.Merchant;
import org.bazar.bazarstore_v2.domain.product.Product;

import java.util.Set;

@Entity
@Table(name = "stores")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Store extends BaseEntity {

    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Product> products;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_resource_id")
    private MediaResource storeLogo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallpaper_resource_id")
    private MediaResource storeWallpaper;

}
