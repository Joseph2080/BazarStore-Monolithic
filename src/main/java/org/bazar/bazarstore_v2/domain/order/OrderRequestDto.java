package org.bazar.bazarstore_v2.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class OrderRequestDto {
    private String customerId;
    private List<OrderItemDto> items;

}
