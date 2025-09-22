package org.bazar.bazarstore_v2.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponseDto {
    private long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int discount;
    private int stock;
    private long storeId;
}
