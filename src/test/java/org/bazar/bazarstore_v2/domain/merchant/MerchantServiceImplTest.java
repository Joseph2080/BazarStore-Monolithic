package org.bazar.bazarstore_v2.domain.merchant;


import org.bazar.bazarstore_v2.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private UserService userService;

    @Mock
    private MerchantDtoMapper merchantDtoMapper;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindMerchantResponseDtoByCognitoUserId_Success() {
        String cognitoUserId = "test-user-id";
        Merchant merchant = Merchant.builder().id(1L).cognitoUUID(cognitoUserId).build();
        MerchantResponseDto responseDto = MerchantResponseDto.builder().merchantId(1L).build();

        when(merchantRepository.findMerchantByCognitoUUID(cognitoUserId)).thenReturn(Optional.of(merchant));
        when(merchantDtoMapper.convertEntityToResponseDto(merchant)).thenReturn(responseDto);

        MerchantResponseDto result = merchantService.findMerchantResponseDtoByCognitoUserId(cognitoUserId);

        assertNotNull(result);
        assertEquals(1L, result.getMerchantId());
        verify(merchantRepository, times(1)).findMerchantByCognitoUUID(cognitoUserId);
    }
}