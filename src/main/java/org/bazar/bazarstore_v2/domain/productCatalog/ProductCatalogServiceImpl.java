package org.bazar.bazarstore_v2.domain.productCatalog;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.bazar.bazarstore_v2.common.util.RepositoryUtil;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResource;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResourceRequestDto;
import org.bazar.bazarstore_v2.domain.mediaResource.MediaResourceService;
import org.bazar.bazarstore_v2.domain.product.Product;
import org.bazar.bazarstore_v2.domain.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

import static org.bazar.bazarstore_v2.common.util.ServiceConstants.PRODUCT_CATALOG_NOT_FOUND_MESSAGE;

@Service
public class ProductCatalogServiceImpl extends AbstractJpaService<
        ProductCatalog,
        Long,
        ProductCatalogRequestDto,
        ProductCatalogResponseDto,
        ProductCatalogRepository>
        implements ProductCatalogService {

    private final ProductService productService;
    private final MediaResourceService mediaResourceService;
    @Value("${s3-product-catalog-bucket}")
    private String PRODUCT_CATALOG_BUCKET;

    @Autowired
    public ProductCatalogServiceImpl(
            ProductCatalogRepository productCatalogRepository,
            ProductCatalogDtoMapper productCatalogDtoMapper,
            MediaResourceService mediaResourceService,
            ProductService productService) {
        super(productCatalogRepository, productCatalogDtoMapper);
        this.productService = productService;
        this.mediaResourceService = mediaResourceService;
    }

    @Override
    protected void setEntityDependencies(ProductCatalog productCatalog, ProductCatalogRequestDto productCatalogRequestDto) {
        Product product = productService.findEntityByIdOrElseThrowException(productCatalogRequestDto.getProductId());
        productCatalog.setProduct(product);
        logger.debug("[{}.setExternalDependencies] Found product id={}", getClass().getSimpleName(), productCatalogRequestDto.getProductId());
        uploadProductCatalogFileToObjectStorage(productCatalog, productCatalogRequestDto.getImageFile());
    }

    @Override
    public void deleteExternalDependencies(ProductCatalog productCatalog) {
        Long id = productCatalog.getId();
        logger.info("[{}.deleteExternalDependencies] Deleting media in S3  id={}", getClass().getSimpleName(), id);
        mediaResourceService.deleteById(productCatalog.getResource().getId());
        logger.debug("[{}.deleteExternalDependencies] Deleted media in S3 for id={}", getClass().getSimpleName(), id);
    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new ProductCatalogNotFoundException(PRODUCT_CATALOG_NOT_FOUND_MESSAGE);
    }

    @Override
    public Set<ProductCatalogResponseDto> findProductCatalogsByProductId(long productId) {
        logger.info("[{}.findProductCatalogsByProductId] Fetching catalogs for productId={}", getClass().getSimpleName(), productId);
        Set<ProductCatalogResponseDto> dtos = repository.findSetByProductId(productId)
                .stream()
                .map(this::buildProductCatalogResponse)
                .collect(Collectors.toSet());
        logger.debug("[{}.findProductCatalogsByProductId] Found {} catalogs for productId={}", getClass().getSimpleName(), dtos.size(), productId);
        return dtos;
    }

    private ProductCatalogResponseDto buildProductCatalogResponse(ProductCatalog productCatalog){
        ProductCatalogResponseDto productCatalogResponseDto =  dtoMapper.convertEntityToResponseDto(productCatalog);
        MediaResource mediaResource = productCatalog.getResource();
        productCatalogResponseDto.setProductImageUrl(mediaResourceService.generatePreSignedUrlForResource(mediaResource.getContext(), mediaResource.getObjectKey()));
        return productCatalogResponseDto;
    }

    private void uploadProductCatalogFileToObjectStorage(ProductCatalog productCatalog, MultipartFile file) {
        if (file == null) {
            logger.warn("[{}.updateProductCatalogImageToS3] No file provided for id={}", getClass().getSimpleName(), productCatalog.getId());
            return;
        }
        MediaResource mediaResource =  productCatalog.getResource();
        Product product = productCatalog.getProduct();
        if (mediaResource == null) {
            mediaResource = createMediaResources(generateKey(product.getId()), file);
            logger.debug("[{}.updateProductCatalogImageToS3] Uploaded new image for id={}", getClass().getSimpleName(), productCatalog.getId());
        } else {
            mediaResource = createMediaResources(mediaResource.getObjectKey(), file);
            logger.debug("[{}.updateProductCatalogImageToS3] Replaced image for id={}", getClass().getSimpleName(), productCatalog.getId());
        }
        productCatalog.setResource(mediaResource);
        logger.info("[{}.updateProductCatalogImageToS3] Update image completed for id={}", getClass().getSimpleName(), productCatalog.getId());
    }

    private MediaResource createMediaResources(String objectKey, MultipartFile multipartFile){
        return mediaResourceService.create(
                MediaResourceRequestDto
                        .builder()
                        .bucket(PRODUCT_CATALOG_BUCKET)
                        .objectKey(objectKey)
                        .multipartFile(multipartFile)
                        .build()
        );
    }

    private String generateKey(Long productId) {
        final String DELIMITER = "_";
        return productId + DELIMITER + RepositoryUtil.generateUUID();
    }
}
