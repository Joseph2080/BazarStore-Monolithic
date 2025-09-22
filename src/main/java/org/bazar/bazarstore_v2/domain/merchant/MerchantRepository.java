package org.bazar.bazarstore_v2.domain.merchant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findMerchantByCognitoUUID(String cognitoUUID);
}
