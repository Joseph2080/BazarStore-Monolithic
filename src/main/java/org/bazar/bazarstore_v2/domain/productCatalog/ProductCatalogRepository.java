package org.bazar.bazarstore_v2.domain.productCatalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface
ProductCatalogRepository extends JpaRepository<ProductCatalog, Long> {
    @Query("SELECT pc FROM ProductCatalog pc WHERE pc.product.id = :productId")
    Set<ProductCatalog> findSetByProductId(Long productId);
}
