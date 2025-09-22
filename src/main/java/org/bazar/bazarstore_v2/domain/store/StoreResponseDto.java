package org.bazar.bazarstore_v2.domain.store;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreResponseDto {
    private long storeId;
    private String name;
    private String description;
    private Long merchantId;
}
