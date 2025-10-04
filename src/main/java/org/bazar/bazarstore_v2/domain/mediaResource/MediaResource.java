package org.bazar.bazarstore_v2.domain.mediaResource;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;

//might need to need to find a way to map a user that uploaded a specific media resource as well.
@Entity
@Table(name = "media_resources")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MediaResource extends BaseJpaEntity {
    @Column(name = "context", nullable = false)
    private String context;
    @Column(name = "object_key", nullable = false, unique = true)
    private String objectKey;
    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "url")
    private String url;
    @Column(name = "size_bytes")
    private Long sizeBytes;
}
