package org.bazar.bazarstore_v2.common.service;


import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Service<Entity extends BaseJpaEntity, ID, RequestDto, ResponseDto> {
    ResponseDto create(RequestDto dto);
    List<ResponseDto> findAll(Pageable pageable);
    void update(ID id, RequestDto dto);
    void deleteById(ID id);
    ResponseDto findByIdOrElseThrowException(ID id);
    void throwExceptionIfEntityByIdDoesNotExist(ID id);
    // fucking dangerous :)
    void deleteAll();
    Entity findEntityByIdOrElseThrowException(ID id);
}
