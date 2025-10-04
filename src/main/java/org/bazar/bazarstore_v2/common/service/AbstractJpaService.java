package org.bazar.bazarstore_v2.common.service;

import org.bazar.bazarstore_v2.common.entity.BaseJpaEntity;
import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.bazar.bazarstore_v2.common.util.RepositoryUtil;
import org.bazar.bazarstore_v2.common.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractJpaService<
        Entity extends BaseJpaEntity,
        ID,
        RequestDTO,
        ResponseDTO,
        Repository extends JpaRepository<Entity, ID>
        >
        implements Service<Entity, ID, RequestDTO, ResponseDTO> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final Repository repository;
    protected final DtoMapper<Entity, RequestDTO, ResponseDTO> dtoMapper;

    public AbstractJpaService(Repository repository,
                           DtoMapper<Entity, RequestDTO, ResponseDTO> dtoMapper) {
        this.repository = repository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ResponseDTO create(RequestDTO requestDTO) {
        logger.info("[{}.create] Creating entity from DTO: {}", getClass().getSimpleName(), requestDTO);
        validateDTO(requestDTO);
        Entity entity = dtoMapper.convertDtoToEntity(requestDTO);
        setEntityDependencies(entity, requestDTO);
        repository.save(entity);
        logger.debug("[{}.create] Entity created: {}", getClass().getSimpleName(), entity);
        return dtoMapper.convertEntityToResponseDto(entity);
    }

    @Override
    public List<ResponseDTO> findAll(Pageable pageable) {
        logger.info("[{}.findAll] Finding all entities with pageable: {}", getClass().getSimpleName(), pageable);
        ValidationUtil.throwIfArgumentIsNull(List.of(pageable));
        List<ResponseDTO> result = transformEntityToDtoList(RepositoryUtil.getPage(repository, pageable).getContent());
        logger.debug("[{}.findAll] Found {} entities", getClass().getSimpleName(), result.size());
        return result;
    }

    @Override
    public void update(ID id, RequestDTO requestDTO) {
        logger.info("[{}.update] Updating entity id={} with DTO: {}", getClass().getSimpleName(), id, requestDTO);
        ValidationUtil.throwIfArgumentIsNull(List.of(id, requestDTO));
        Entity entity = findEntityByIdOrElseThrowException(id);
        dtoMapper.updateEntityFromDto(requestDTO, entity);
        setEntityDependencies(entity, requestDTO);
        repository.save(entity);
        logger.debug("[{}.update] Entity id={} updated", getClass().getSimpleName(), id);
    }

    @Override
    public void deleteById(ID id) {
        logger.info("[{}.deleteById] Deleting entity id={}", getClass().getSimpleName(), id);
        ValidationUtil.throwIfArgumentIsNull(id);
        Entity entity = findEntityByIdOrElseThrowException(id);
        repository.delete(entity);
        deleteExternalDependencies(entity);
        logger.debug("[{}.deleteById] Entity id={} deletion attempted", getClass().getSimpleName(), id);
    }

    @Override
    public ResponseDTO findByIdOrElseThrowException(ID id) {
        logger.info("[{}.findById] Finding entity DTO by id={}", getClass().getSimpleName(), id);
        ValidationUtil.throwIfArgumentIsNull(id);
        ResponseDTO dto = dtoMapper.convertEntityToResponseDto(findEntityByIdOrElseThrowException(id));
        logger.debug("[{}.findById] Found entity DTO: {}", getClass().getSimpleName(), dto);
        return dto;
    }

    @Override
    public Entity findEntityByIdOrElseThrowException(ID id) {
        logger.info("[{}.findEntityById] Finding entity by id={}", getClass().getSimpleName(), id);
        ValidationUtil.throwIfArgumentIsNull(id);
        Entity entity = RepositoryUtil.findEntityByIdOrElseThrow(repository, id, entityNotFoundException());
        logger.debug("[{}.findEntityById] Found entity: {}", getClass().getSimpleName(), entity);
        return entity;
    }

    @Override
    public void throwExceptionIfEntityByIdDoesNotExist(ID id) {
        logger.info("[{}.exists] Checking existence of entity id={}", getClass().getSimpleName(), id);
        if (!repository.existsById(id)) {
            logger.error("[{}.exists] Entity id={} not found, throwing exception", getClass().getSimpleName(), id);
            throw entityNotFoundException();
        }
        logger.debug("[{}.exists] Entity id={} exists", getClass().getSimpleName(), id);
    }

    @Override
    public void deleteAll() {
        logger.info("[{}.deleteAll] Deleting all entities", getClass().getSimpleName());
        RepositoryUtil.deleteAll(repository);
        logger.debug("[{}.deleteAll] All entities deletion attempted", getClass().getSimpleName());
    }

    protected void setEntityDependencies(Entity entity, RequestDTO requestDTO) {
        logger.debug("[{}.setExternalDependencies] No external dependencies to set", getClass().getSimpleName());
    }

    protected void validateDTO(RequestDTO requestDTO) {
        logger.info("[{}.validateDTO] Validating request dto for non-null values: {}", getClass().getSimpleName(), requestDTO);
        ValidationUtil.validateDtoFields(requestDTO);
        applyCustomValidation(requestDTO);
    }

    protected void applyCustomValidation(RequestDTO requestDTO) {
        logger.debug("[{}.applyCustomValidation] No additional custom validation has been set", getClass().getSimpleName());
    }

    protected void deleteExternalDependencies(Entity entity) {
        logger.debug("[{}.deleteExternalDependencies] No external dependencies have been deleted", getClass().getSimpleName());
    }

    protected List<ResponseDTO> transformEntityToDtoList(List<Entity> entityList) {
        logger.debug("[{}.transformEntityToDtoList] Transforming list of entities to DTOs", getClass().getSimpleName());
        return entityList.stream()
                .map(dtoMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    protected abstract EntityNotFoundException entityNotFoundException();

}
