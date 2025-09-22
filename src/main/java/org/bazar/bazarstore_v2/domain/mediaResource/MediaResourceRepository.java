package org.bazar.bazarstore_v2.domain.mediaResource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaResourceRepository extends JpaRepository<MediaResource, Long> {
    Optional<MediaResource> findByObjectKey(String objectKey);
}
