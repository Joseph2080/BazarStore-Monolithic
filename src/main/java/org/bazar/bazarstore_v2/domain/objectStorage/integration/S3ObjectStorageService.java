package org.bazar.bazarstore_v2.domain.objectStorage.integration;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.bazar.bazarstore_v2.common.util.ValidationUtil;
import org.bazar.bazarstore_v2.domain.objectStorage.ObjectStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class S3ObjectStorageService implements ObjectStorageService {
    private static final Logger logger = LoggerFactory.getLogger(S3ObjectStorageService.class);
    private final AmazonS3 amazonS3Client;

    private static final List<String> ALLOWED_FILE_TYPES = List.of("jpg", "jpeg", "png", "gif");
    private static final long DEFAULT_URL_EXPIRATION_MINUTES = 15;

    @Autowired
    public S3ObjectStorageService(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public String uploadObject(String bucket, String objectId, MultipartFile multipartFile) {
        logger.info("[{}.uploadObject] Uploading objectId={} to bucket={}", getClass().getSimpleName(), objectId, bucket);
        ValidationUtil.throwIfArgumentListIsNull(List.of(objectId, multipartFile));
        validateFile(multipartFile.getOriginalFilename());

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            amazonS3Client.putObject(new PutObjectRequest(bucket, objectId, multipartFile.getInputStream(), metadata));
            String url = amazonS3Client.getUrl(bucket, objectId).toString();
            logger.debug("[{}.uploadObject] Uploaded and generated URL: {}", getClass().getSimpleName(), url);
            return url;
        } catch (IOException e) {
            logger.error("[{}.uploadObject] Upload failed for objectId={} in bucket={}. Error: {}",
                    getClass().getSimpleName(), objectId, bucket, e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public String generatePreSignedUrlForObject(String bucket, String objectId) {
        logger.info("[{}.downloadObject] Generating pre-signed URL for objectId={} in bucket={}",
                getClass().getSimpleName(), objectId, bucket);
        ValidationUtil.throwIfArgumentIsNull(objectId);

        try {
            String presignedUrl = generatePreSignedUrl(bucket, objectId);
            logger.debug("[{}.downloadObject] Pre-signed URL: {}", getClass().getSimpleName(), presignedUrl);
            return presignedUrl;
        } catch (Exception e) {
            logger.error("[{}.downloadObject] Failed to generate pre-signed URL for objectId={} in bucket={}. Error: {}",
                    getClass().getSimpleName(), objectId, bucket, e.getMessage(), e);
            return "pay your AWS bill, stop being cheap :)";
        }
    }

    private String generatePreSignedUrl(String bucket, String objectId) {
        Date expiration = getExpirationDate();
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket, objectId)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        return amazonS3Client.generatePresignedUrl(urlRequest).toString();
    }

    @Override
    public void deleteObject(String bucket, String objectId) {
        logger.info("[{}.deleteObject] Deleting objectId={} from bucket={}", getClass().getSimpleName(), objectId, bucket);
        ValidationUtil.throwIfArgumentListIsNull(List.of(bucket, objectId));
        try {
            amazonS3Client.deleteObject(bucket, objectId);
            logger.debug("[{}.deleteObject] Deleted objectId={} from bucket={}", getClass().getSimpleName(), objectId, bucket);
        } catch (Exception e) {
            logger.error("[{}.deleteObject] Failed to delete objectId={} from bucket={}. Error: {}",
                    getClass().getSimpleName(), objectId, bucket, e.getMessage(), e);
            throw new RuntimeException("Failed to delete object " + objectId, e);
        }
    }

    private Date getExpirationDate() {
        Instant expiration = Instant.now().plus(Duration.ofMinutes(S3ObjectStorageService.DEFAULT_URL_EXPIRATION_MINUTES));
        return Date.from(expiration);
    }

    private void validateFile(String originalFilename) {
        logger.info("[{}.validateFile] Validating file: {}", getClass().getSimpleName(), originalFilename);
        String fileExtension = getFileExtensionFromFilename(originalFilename);
        if (!ALLOWED_FILE_TYPES.contains(fileExtension.toLowerCase())) {
            logger.error("[{}.validateFile] Invalid file extension: {}", getClass().getSimpleName(), fileExtension);
            throw new IllegalArgumentException(originalFilename + " cannot be saved. Allowed types: " + ALLOWED_FILE_TYPES);
        }
        logger.debug("[{}.validateFile] File extension {} is valid", getClass().getSimpleName(), fileExtension);
    }

    private String getFileExtensionFromFilename(String originalFilename) {
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            logger.error("[{}.getFileExtensionFromFilename] No extension found: {}", getClass().getSimpleName(), originalFilename);
            throw new IllegalArgumentException("Invalid file: no extension found");
        }
        return originalFilename.substring(lastDotIndex + 1);
    }
}
