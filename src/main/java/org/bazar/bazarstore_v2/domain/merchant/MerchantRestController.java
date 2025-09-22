package org.bazar.bazarstore_v2.domain.merchant;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.buildResponse;

@Tag(name = "Merchant API", description = "API for managing merchants")
@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantRestController {
    private final MerchantService merchantService;

    @Autowired
    public MerchantRestController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Operation(summary = "get merchant by ID", description = "Fetch a merchant's details using their ID.")
    @ApiResponse(responseCode = "200", description = "Merchant found successfully")
    @GetMapping("/{merchantId}")
    public ResponseEntity<Map<String, Object>> getMerchantById(@PathVariable Long merchantId) {
        return buildResponse(merchantService.findByIdOrElseThrowException(merchantId), HttpStatus.OK, "Merchant has been found successfully.");
    }

    @Operation(summary = "Create a new merchant", description = "Registers a new merchant in the system.")
    @ApiResponse(responseCode = "201", description = "Merchant created successfully")
    @PostMapping
    public ResponseEntity<Map<String, Object>> postMerchant(@RequestBody MerchantRequestDto merchantRequestDto) {
        return buildResponse(merchantService.create(merchantRequestDto), HttpStatus.CREATED, "Merchant has been created successfully.");
    }

    @Operation(summary = "Update an existing merchant", description = "Updates the details of a merchant by ID.")
    @ApiResponse(responseCode = "200", description = "Merchant updated successfully")
    @PutMapping("/{merchantId}")
    public ResponseEntity<Map<String, Object>> putMerchant(@PathVariable Long merchantId,
                                                           @RequestBody MerchantRequestDto merchantRequestDto) {
        merchantService.update(merchantId, merchantRequestDto);
        return buildResponse(null, HttpStatus.OK, "Merchant has been updated successfully.");
    }

    @Operation(summary = "Get paginated list of merchants", description = "Fetch a paginated list of merchants.")
    @ApiResponse(responseCode = "200", description = "Merchants retrieved successfully")
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllMerchants(Pageable pageable) {
        return buildResponse(merchantService.findAll(pageable),
                HttpStatus.OK,
                "merchants have been found successfully.");
    }

    @Operation(summary = "Get merchant by user ID", description = "Fetch a merchant's details using the user's ID.")
    @ApiResponse(responseCode = "200", description = "Merchant found successfully")
    @GetMapping("/by-userid")
    public ResponseEntity<Map<String, Object>> getMerchantByUserId(@RequestParam String userId) {
        return buildResponse(merchantService.findMerchantResponseDtoByCognitoUserId(userId), HttpStatus.OK, "Merchant has been found.");
    }

    @Operation(summary = "Delete a merchant", description = "Deletes a merchant by ID.")
    @ApiResponse(responseCode = "200", description = "Merchant deleted successfully")
    @DeleteMapping("/{merchantId}")
    public ResponseEntity<Map<String, Object>> deleteMerchant(@PathVariable Long merchantId) {
        merchantService.deleteById(merchantId);
        return buildResponse(null, HttpStatus.OK, "Merchant has been deleted successfully.");
    }
}
