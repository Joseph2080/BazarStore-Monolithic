package org.bazar.bazarstore_v2.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String customerId;
    private BigDecimal totalAmount;
}
