package org.bazar.bazarstore_v2.domain.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.buildResponse;

@Tag(name = "Store API", description = "API for managing stores")
@RestController
@RequestMapping("/api/v1/stores")
public class StoreRestController {
    private final StoreService storeService;

    @Autowired
    public StoreRestController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "Get store by ID", description = "Fetch store details using its ID.")
    @ApiResponse(responseCode = "200", description = "Store found successfully")
    @GetMapping("/{storeId}")
    public ResponseEntity<Map<String, Object>> getStoreById(@PathVariable Long storeId) {
        return buildResponse(storeService.findByIdOrElseThrowException(storeId), HttpStatus.OK, "Store has been found successfully.");
    }

    @Operation(summary = "Create a new store", description = "Registers a new store in the system.")
    @ApiResponse(responseCode = "200", description = "Store created successfully")
    @PostMapping
    public ResponseEntity<Map<String, Object>> postStore(@RequestBody StoreRequestDto storeRequestDto) {
        return buildResponse(storeService.create(storeRequestDto), HttpStatus.OK, "Store has been created successfully.");
    }

    @Operation(summary = "Update store wallpaper", description = "Updates the wallpaper of a store.")
    @ApiResponse(responseCode = "200", description = "Wallpaper updated successfully")
    @PostMapping("/{storeId}/wallpaper")
    public ResponseEntity<Map<String, Object>> postStoreWallpaper(@PathVariable Long storeId,
                                                                  @RequestPart MultipartFile storeWallpaperMultiPartFile) {
        storeService.updateWallpaper(storeId, storeWallpaperMultiPartFile);
        return buildResponse(null, HttpStatus.OK, "Wallpaper has been updated successfully.");
    }

    @Operation(summary = "Update store logo", description = "Updates the logo of a store.")
    @ApiResponse(responseCode = "200", description = "Logo updated successfully")
    @PostMapping("/{storeId}/logo")
    public ResponseEntity<Map<String, Object>> postStoreLogo(@PathVariable Long storeId,
                                                             @RequestPart MultipartFile storeLogoMultiPartFile) {
        storeService.updateLogo(storeId, storeLogoMultiPartFile);
        return buildResponse(null, HttpStatus.OK, "Logo has been updated successfully.");
    }

    @Operation(summary = "Get paginated list of stores", description = "Fetch a paginated list of stores.")
    @ApiResponse(responseCode = "200", description = "Stores retrieved successfully")
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllStores(Pageable pageable) {
        return buildResponse(storeService.findAll(pageable), HttpStatus.OK, "All stores have been fetched successfully.");
    }

    @Operation(summary = "Get stores by merchant ID", description = "Fetch all stores linked to a merchant ID.")
    @ApiResponse(responseCode = "200", description = "Stores retrieved successfully")
    @GetMapping("/by-merchant")
    public ResponseEntity<Map<String, Object>> getStoresByMerchantId(@RequestParam long merchantId) {
        return buildResponse(storeService.findAllStoresByMerchantId(merchantId), HttpStatus.OK, "All stores have been fetched by merchant ID successfully.");
    }

    @Operation(summary = "Delete a store", description = "Deletes a store by ID.")
    @ApiResponse(responseCode = "200", description = "Store deleted successfully")
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteStore(@RequestBody Long storeId) {
        storeService.deleteById(storeId);
        return buildResponse(null, HttpStatus.OK, "Store has been deleted successfully.");
    }

    @Operation(summary = "Get store wallpaper URL", description = "Returns a presigned URL to access the store's wallpaper.")
    @ApiResponse(responseCode = "200", description = "Presigned URL returned successfully")
    @GetMapping("/{storeId}/wallpaper/url")
    public ResponseEntity<Map<String, Object>> getStoreWallpaperPresignedUrl(@PathVariable Long storeId) {
        return buildResponse(storeService.generateWallpaperPresignedUrl(storeId), HttpStatus.OK, "Presigned URL generated successfully.");
    }

    @Operation(summary = "Get store wallpaper URL", description = "Returns a presigned URL to access the store's wallpaper.")
    @ApiResponse(responseCode = "200", description = "Presigned URL returned successfully")
    @GetMapping("/{storeId}/logo/url")
    public ResponseEntity<Map<String, Object>> getStoreLogoPresignedUrl(@PathVariable Long storeId) {
        return buildResponse(storeService.generateLogoPresignedUrl(storeId), HttpStatus.OK, "Presigned URL generated successfully.");
    }
}
