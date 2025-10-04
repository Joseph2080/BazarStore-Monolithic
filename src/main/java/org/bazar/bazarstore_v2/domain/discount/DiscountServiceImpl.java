package org.bazar.bazarstore_v2.domain.discount;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.bazar.bazarstore_v2.domain.product.ProductService;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl extends AbstractJpaService<
        Discount,
        Long,
        DiscountRequestDto,
        DiscountResponseDto,
        DiscountRepository> implements DiscountService {

    private ProductService productService;

    public DiscountServiceImpl(DiscountRepository repository,
                               DiscountDtoMapper mapper,
                               ProductService productService) {
        super(repository, mapper);
        this.productService = productService;
    }

    @Override
    protected void setEntityDependencies(Discount entity, DiscountRequestDto discountRequestDto) {
        discountRequestDto.getProductIdSet().forEach(productId -> {
            try {
                entity.getApplicableProducts().add(productService.findEntityByIdOrElseThrowException(productId));
            } catch (EntityNotFoundException entityNotFoundException) {
                logger.debug("Product with id {} not found. Skipping adding to discount ", productId);
            }
        });
    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new DiscountNotFoundException("Discount not found");
    }

    @Override
    public DiscountResponseDto findDiscountByCode(String code) {
        return dtoMapper.convertEntityToResponseDto(repository.findByCode(code)
                .orElseThrow(() -> new DiscountNotFoundException("Discount with code " + code + " not found")));
    }
}
