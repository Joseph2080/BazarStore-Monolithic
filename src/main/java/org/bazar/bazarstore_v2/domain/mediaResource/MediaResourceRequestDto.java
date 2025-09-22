package org.bazar.bazarstore_v2.domain.mediaResource;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class MediaResourceRequestDto {

    @NotEmpty(message = "bucket cannot be empty")
    private String bucket;

    @NotEmpty(message = "objectKey cannot be empty")
    private String objectKey;

    private MultipartFile multipartFile;
}
