package org.bazar.bazarstore_v2.domain.mediaResource;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.bazar.bazarstore_v2.domain.objectStorage.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.bazar.bazarstore_v2.common.util.ServiceConstants.MEDIA_RESOURCE_NOT_FOUND_MESSAGE;


@Service
public class MediaResourceServiceImpl extends AbstractJpaService<
        MediaResource,
        Long,
        MediaResourceRequestDto,
        MediaResource,
        MediaResourceRepository>
        implements MediaResourceService {

    protected final ObjectStorageService objectStorageService;

    @Autowired
    public MediaResourceServiceImpl(MediaResourceRepository mediaResourceRepository,
                                    MediaResourceMapper mediaResourceMapper,
                                    ObjectStorageService objectStorageService) {
        super(mediaResourceRepository, mediaResourceMapper);
        this.objectStorageService = objectStorageService;
    }

    @Override
    public void setEntityDependencies(MediaResource mediaResource, MediaResourceRequestDto mediaResourceRequestDto) {
        String bucket = mediaResourceRequestDto.getBucket();
        String objectKey = mediaResourceRequestDto.getObjectKey();
        MultipartFile multipartFile = mediaResourceRequestDto.getMultipartFile();
        String url = objectStorageService.uploadObject(bucket, objectKey, multipartFile);
        mediaResource.setUrl(url);
    }

    @Override
    public void deleteByObjectKey(String objectKey) {
        MediaResource mediaResource = findByObjectKey(objectKey);
        objectStorageService.deleteObject(mediaResource.getContext(), objectKey);
        repository.delete(mediaResource);
    }

    @Override
    protected void deleteExternalDependencies(MediaResource mediaResource) {
        objectStorageService.deleteObject(mediaResource.getContext(), mediaResource.getObjectKey());
    }

    public MediaResource findByObjectKey(String objectKey) {
        return repository.findByObjectKey(objectKey)
                .orElseThrow(() -> new MediaResourceNotFoundException("Resource not found for key: " + objectKey));
    }

    @Override
    public String generatePreSignedUrlForResource(Long id) {
        MediaResource mediaResource = findByIdOrElseThrowException(id);
        return objectStorageService.generatePreSignedUrlForObject(mediaResource.getContext(), mediaResource.getObjectKey());
    }

    @Override
    public String generatePreSignedUrlForResource(String context, String objectKey) {
        return objectStorageService.generatePreSignedUrlForObject(context, objectKey);
    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new MediaResourceNotFoundException(MEDIA_RESOURCE_NOT_FOUND_MESSAGE);
    }
}
