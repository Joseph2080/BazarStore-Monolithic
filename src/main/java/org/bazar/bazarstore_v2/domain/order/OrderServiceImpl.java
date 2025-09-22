package org.bazar.bazarstore_v2.domain.order;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.mapper.DtoMapper;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends AbstractJpaService<
        Order,
        Long,
        OrderRequestDto,
        OrderResponseDto,
        OrderRepository> implements OrderService {

    public OrderServiceImpl(OrderRepository repository, DtoMapper<Order, OrderRequestDto, OrderResponseDto> dtoMapper) {
        super(repository, dtoMapper);
    }

    @Override
    protected void applyCustomValidation(OrderRequestDto merchantRequestDto) {

    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new OrderNotFoundException("Order could not be found");
    }

    @Override
    public void updateOrderStatusByOrderId(Long id, String status) {
        Order orderById = findEntityByIdOrElseThrowException(id);
        orderById.setStatus(status);
        repository.save(orderById);
    }
}
