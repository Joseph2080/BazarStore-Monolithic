package org.bazar.bazarstore_v2.domain.merchant;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;

@Builder
@Entity
@Table(name = "merchants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Merchant extends BaseJpaEntity {

    private String taxId;
    private String firstname;
    private String middleName;
    private String lastName;
    private String cognitoUUID;

}
