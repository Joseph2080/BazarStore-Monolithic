package org.bazar.bazarstore_v2.domain.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponseDto {
    private Long id;
    private BigDecimal percentage;
    private Set<Long> productIdSet;
    private boolean isValid;
}
