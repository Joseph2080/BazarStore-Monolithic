package org.bazar.bazarstore_v2.domain.product;

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
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.buildResponse;

@Tag(name = "Product API", description = "API for managing products")
@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product by ID", description = "Fetch a product's details using its ID.")
    @ApiResponse(responseCode = "200", description = "Product found successfully")
    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long productId) {
        return buildResponse(productService.findByIdOrElseThrowException(productId), HttpStatus.OK, "Product has been found successfully.");
    }

    @Operation(summary = "Create a new product", description = "Registers a new product in the system.")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping
    public ResponseEntity<Map<String, Object>> postProduct(@RequestBody ProductRequestDto productRequestDto) {
        return buildResponse(productService.create(productRequestDto), HttpStatus.CREATED, productRequestDto.getName() + " has been created successfully.");
    }

    @Operation(summary = "Get paginated list of products", description = "Fetch a paginated list of products.")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllProducts(Pageable pageable) {
        return buildResponse(productService.findAll(pageable), HttpStatus.OK, "All products have been fetched successfully.");
    }

    @Operation(summary = "Get products by store ID", description = "Fetch all products associated with a given store ID.")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/by-store")
    public ResponseEntity<Map<String, Object>> getAllProductsByStoreId(@RequestParam Long storeId) {
        return buildResponse(productService.findProductsByStoreId(storeId), HttpStatus.OK, "All products by store have been fetched successfully.");
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by ID.")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        return buildResponse(null, HttpStatus.OK, productId + " has been deleted successfully.");
    }
}