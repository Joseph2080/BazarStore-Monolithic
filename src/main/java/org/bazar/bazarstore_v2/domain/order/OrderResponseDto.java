package org.bazar.bazarstore_v2.domain.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponseDto {
    private Long orderId;
    private String orderStatus;
    private BigDecimal amount;
}
