package org.bazar.bazarstore_v2.domain.objectStorage;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageService {
    String uploadObject(String context, String imageName, MultipartFile file);
    void deleteObject(String context, String imageId);
    String generatePreSignedUrlForObject(String context, String imageId);
}
