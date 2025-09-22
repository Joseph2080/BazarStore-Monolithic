package org.bazar.bazarstore_v2.domain.mediaResource;

import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MediaResourceMapper implements DtoMapper<MediaResource, MediaResourceRequestDto, MediaResource> {

    @Override
    public MediaResource convertDtoToEntity(MediaResourceRequestDto mediaResourceRequestDto) {
        MultipartFile multipartFile = mediaResourceRequestDto.getMultipartFile();
        return MediaResource.builder()
                .context(mediaResourceRequestDto.getBucket())
                .objectKey(mediaResourceRequestDto.getObjectKey())
                .originalFilename(multipartFile.getOriginalFilename())
                .contentType(multipartFile.getContentType())
                .sizeBytes(multipartFile.getSize())
                .build();
    }

    @Override
    public MediaResource convertEntityToResponseDto(MediaResource mediaResource) {
        //for now I don't need this since we are only communicating service to service layer at the moment and we need the raw entity
        return mediaResource;
    }

    @Override
    public void updateEntityFromDto(MediaResourceRequestDto mediaResourceRequestDto, MediaResource mediaResource) {
        MultipartFile multipartFile = mediaResourceRequestDto.getMultipartFile();
        mediaResource.setContext(mediaResourceRequestDto.getBucket());
        mediaResource.setObjectKey(mediaResourceRequestDto.getObjectKey());
        mediaResource.setOriginalFilename(multipartFile.getOriginalFilename());
        mediaResource.setContentType(multipartFile.getContentType());
        mediaResource.setSizeBytes(multipartFile.getSize());
    }
}
