
package org.bazar.bazarstore_v2.domain.merchant;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.exception.InvalidFieldException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.bazar.bazarstore_v2.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.bazar.bazarstore_v2.common.util.ServiceConstants.INVALID_USER_ID;
import static org.bazar.bazarstore_v2.common.util.ServiceConstants.MERCHANT_NOT_FOUND_MESSAGE;

@Service
public class MerchantServiceImpl extends AbstractJpaService<Merchant,
        Long,
        MerchantRequestDto,
        MerchantResponseDto,
        MerchantRepository> implements MerchantService {

    private final UserService userService;

    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository,
                               UserService userService,
                               MerchantDtoMapper merchantDtoMapper) {
        super(merchantRepository, merchantDtoMapper);
        this.userService = userService;
    }

    @Override
    protected void applyCustomValidation(MerchantRequestDto merchantRequestDto) {
        if (!userService.doesUserExistsById(merchantRequestDto.getUserId())) {
            logger.warn("[{}.validateDTO] Invalid CognitoUserId: {}", getClass().getSimpleName(), merchantRequestDto.getUserId());
            throw new InvalidFieldException(INVALID_USER_ID);
        }
        ensureCognitoUserIdIsNotAlreadyRegistered(merchantRequestDto.getUserId());
    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new MerchantNotFoundException(MERCHANT_NOT_FOUND_MESSAGE);
    }

    @Override
    public MerchantResponseDto findMerchantResponseDtoByCognitoUserId(String cognitoUserId) {
        logger.info("[{}.findMerchantResponseDtoByCognitoUserId] Fetching merchant for CognitoUserId: {}", getClass().getSimpleName(), cognitoUserId);
        Merchant merchant = findMerchantByCognitoUserIdOrElseThrowNotFoundException(cognitoUserId);
        MerchantResponseDto response = dtoMapper.convertEntityToResponseDto(merchant);
        logger.debug("[{}.findMerchantResponseDtoByCognitoUserId] Found MerchantResponseDto: {}", getClass().getSimpleName(), response);
        return response;
    }

    private Merchant findMerchantByCognitoUserIdOrElseThrowNotFoundException(String cognitoUserId) {
        logger.debug("[{}.findMerchantByCognitoUserIdOrElseThrowNotFoundException] Searching for merchant by CognitoUserId: {}",
                getClass().getSimpleName(),
                cognitoUserId);
        return repository.findMerchantByCognitoUUID(cognitoUserId)
                .orElseThrow(() -> {
                    logger.error("[{}.findMerchantByCognitoUserIdOrElseThrowNotFoundException] Merchant not found for CognitoUserId: {}",
                            getClass().getSimpleName(),
                            cognitoUserId);
                    return entityNotFoundException();
                });
    }

    private void ensureCognitoUserIdIsNotAlreadyRegistered(String cognitoUserId) {
        logger.debug("[{}.ensureCognitoUserIdIsNotAlreadyRegistered] Checking registration for CognitoUserId: {}", getClass().getSimpleName(), cognitoUserId);
        repository.findMerchantByCognitoUUID(cognitoUserId).ifPresent(merchant -> {
            logger.warn("[{}.ensureCognitoUserIdIsNotAlreadyRegistered] CognitoUserId already registered: {}", getClass().getSimpleName(), cognitoUserId);
            throw new InvalidFieldException(INVALID_USER_ID);
        });
    }
}
