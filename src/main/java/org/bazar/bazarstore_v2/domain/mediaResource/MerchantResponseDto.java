package org.bazar.bazarstore_v2.domain.mediaResource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantResponseDto {
    private long merchantId;
    private String taxId;
    private String firstname;
    private String middleName;
    private String lastName;
    private String cognitoUserId;
}
