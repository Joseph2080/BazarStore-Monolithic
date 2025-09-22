package org.bazar.bazarstore_v2.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequestDto {
    private String customerId;
    private List<OrderItem> items;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
}
