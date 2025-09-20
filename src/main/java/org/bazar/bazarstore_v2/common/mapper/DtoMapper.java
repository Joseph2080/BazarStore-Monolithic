package org.bazar.bazarstore_v2.common.mapper;

public interface DtoMapper<Entity, RequestDto, ResponseDto> {
    Entity convertDtoToEntity(RequestDto dto);

    ResponseDto convertEntityToResponseDto(Entity entity);

    void updateEntityFromDto(RequestDto dto, Entity entity);
}
