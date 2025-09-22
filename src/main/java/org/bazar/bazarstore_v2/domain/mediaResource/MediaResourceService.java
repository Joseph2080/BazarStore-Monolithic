package org.bazar.bazarstore_v2.domain.mediaResource;


import org.bazar.bazarstore_v2.common.service.Service;

public interface MediaResourceService extends Service<MediaResource, Long, MediaResourceRequestDto, MediaResource> {
    String generatePreSignedUrlForResource(Long resourceId);
    String generatePreSignedUrlForResource(String context, String objectKey);
    void deleteByObjectKey(String objectKey);
}
