package org.bazar.bazarstore_v2.domain.store;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.bazar.bazarstore_v2.common.util.RepositoryUtil;
import org.bazar.bazarstore_v2.common.util.ValidationUtil;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResource;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResourceNotFoundException;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResourceRequestDto;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResourceService;
import org.bazar.bazarstore_v2.domain.merchant.Merchant;
import org.bazar.bazarstore_v2.domain.merchant.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.bazar.bazarstore_v2.common.util.ServiceConstants.STORE_NOT_FOUND_MESSAGE;

//import static com.bazar.bazarstore.service.constants.ServiceConstants.STORE_NOT_FOUND_MESSAGE;

@Service
public class StoreServiceImpl extends AbstractJpaService<
        Store,
        Long,
        StoreRequestDto,
        StoreResponseDto,
        StoreRepository>
        implements StoreService {

    private final MerchantService merchantService;
    private final MediaResourceService mediaResourceService;

    @Value("${S3-wallpapers-bucket}")
    private String WALLPAPER_BUCKET;

    @Value("${S3-logos-bucket}")
    private String LOGO_BUCKET;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository,
                            StoreDtoMapper storeDtoMapper,
                            MerchantService merchantService,
                            MediaResourceService mediaResourceService) {
        super(storeRepository, storeDtoMapper);
        this.merchantService = merchantService;
        this.mediaResourceService = mediaResourceService;
    }

    @Override
    protected void setEntityDependencies(Store store, StoreRequestDto storeRequestDto) {
        logger.debug("[{}.setExternalDependencies] Setting merchant for store with merchantId={}",
                getClass().getSimpleName(), storeRequestDto.getMerchantId());
        Merchant merchant = merchantService.findEntityByIdOrElseThrowException(storeRequestDto.getMerchantId());
        store.setMerchant(merchant);
        logger.debug("[{}.setExternalDependencies] Merchant set successfully for store", getClass().getSimpleName());
    }

    @Override
    public void updateWallpaper(Long storeId, MultipartFile wallpaper) {
        logger.info("[{}.updateWallpaper] Updating wallpaper for storeId={}", getClass().getSimpleName(), storeId);
        ValidationUtil.throwIfArgumentIsNull(List.of(storeId, wallpaper));
        Store store = findEntityByIdOrElseThrowException(storeId);

        String objectKey = generateOrReuseKey(store.getStoreWallpaper(), storeId);
        MediaResource updated = getMediaResource(objectKey, WALLPAPER_BUCKET, wallpaper);

        store.setStoreWallpaper(updated);
        repository.save(store);
        logger.info("[{}.updateWallpaper] Wallpaper update completed for storeId={}", getClass().getSimpleName(), storeId);
    }

    @Override
    public void updateLogo(Long storeId, MultipartFile logo) {
        logger.info("[{}.updateLogo] Updating logo for storeId={}", getClass().getSimpleName(), storeId);
        ValidationUtil.throwIfArgumentIsNull(List.of(storeId, logo));
        Store store = findEntityByIdOrElseThrowException(storeId);

        String objectKey = generateOrReuseKey(store.getStoreLogo(), storeId);
        MediaResource updated = getMediaResource(objectKey, LOGO_BUCKET, logo);

        store.setStoreLogo(updated);
        repository.save(store);
        logger.info("[{}.updateLogo] Logo update completed for storeId={}", getClass().getSimpleName(), storeId);
    }

    private MediaResource getMediaResource(String objectKey, String bucket, MultipartFile logo) {
        return mediaResourceService.create(MediaResourceRequestDto.builder()
                .bucket(bucket)
                .objectKey(objectKey)
                .multipartFile(logo)
                .build());
    }

    @Override
    public String generateWallpaperPresignedUrl(Long storeId) {
        Store store = findEntityByIdOrElseThrowException(storeId);
        MediaResource wallpaper = store.getStoreWallpaper();
        if (wallpaper == null) {
            throw new MediaResourceNotFoundException("No wallpaper found for storeId=" + storeId);
        }
        return mediaResourceService.generatePreSignedUrlForResource(wallpaper.getContext(), wallpaper.getObjectKey());
    }

    @Override
    public String generateLogoPresignedUrl(Long storeId) {
        Store store = findEntityByIdOrElseThrowException(storeId);
        MediaResource logo = store.getStoreLogo();
        if (logo == null) {
            throw new MediaResourceNotFoundException("No logo found for storeId=" + storeId);
        }
        return mediaResourceService.generatePreSignedUrlForResource(logo.getContext(), logo.getObjectKey());
    }

    @Override
    public List<StoreResponseDto> findAllStoresByMerchantId(Long merchantId) {
        ValidationUtil.throwIfArgumentIsNull(merchantId);
        List<Store> stores = repository.findStoresByMerchantId(merchantId)
                .orElseThrow(() -> {
                    logger.error("[{}.findAllStoresByMerchantId] No stores found for merchantId={}", getClass().getSimpleName(), merchantId);
                    return entityNotFoundException();
                });
        return transformEntityToDtoList(stores);
    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new StoreNotFoundException(STORE_NOT_FOUND_MESSAGE);
    }

    private String generateOrReuseKey(MediaResource existingResource, Long storeId) {
        return existingResource != null
                ? existingResource.getObjectKey()
                : storeId + "_" + RepositoryUtil.generateUUID();
    }
}
