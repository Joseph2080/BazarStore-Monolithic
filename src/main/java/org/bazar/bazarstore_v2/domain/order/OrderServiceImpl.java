package org.bazar.bazarstore_v2.domain.order;

import org.bazar.bazarstore_v2.common.exception.EntityNotFoundException;
import org.bazar.bazarstore_v2.common.service.AbstractJpaService;
import org.bazar.bazarstore_v2.domain.discount.DiscountService;
import org.bazar.bazarstore_v2.domain.payment.PaymentResponse;
import org.bazar.bazarstore_v2.domain.payment.PaymentStatus;
import org.bazar.bazarstore_v2.domain.product.ProductService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends AbstractJpaService<
        Order,
        Long,
        OrderRequestDto,
        OrderResponseDto,
        OrderRepository> implements OrderService {

    private ProductService productService;
    private DiscountService discountService;
    //need to implement a dto mapper for order-service as well
    public OrderServiceImpl(OrderRepository repository,
                            OrderDtoMapper dtoMapper,
                            ProductService productService,
                            DiscountService discountService) {
        super(repository, dtoMapper);
        this.productService = productService;
        this.discountService = discountService;
    }

    @Override
    protected void applyCustomValidation(OrderRequestDto orderRequestDto) {
        //maybe verify the seller id here in case they have a missing item or something
    }

    @Override
    public void setEntityDependencies(Order order, OrderRequestDto orderRequestDto) {
        order.setItems(
                orderRequestDto
                .getItems()
                .stream()
                .map(item -> convertOrderItemDtoToOrderItemEntity(order, item))
                .toList()
        );
    }

    private OrderItem convertOrderItemDtoToOrderItemEntity(Order order, OrderItemDto orderItemDto) {
        return OrderItem.builder()
                .product(productService.findEntityByIdOrElseThrowException(orderItemDto.getProductId()))
                .order(order)
                .discount(discountService.findEntityByIdOrElseThrowException(orderItemDto.getDiscountId()))
                .quantity(orderItemDto.getQuantity())
                .build();
    }

    @Override
    protected EntityNotFoundException entityNotFoundException() {
        return new OrderNotFoundException("Order could not be found");
    }

    @Override
    public OrderStatus updateOrderStatusByOrderId(Long id, PaymentResponse paymentResponse) {
        Order orderById = findEntityByIdOrElseThrowException(id);
        OrderStatus orderStatus = paymentResponse.getStatus() == PaymentStatus.SUCCEEDED
                ? OrderStatus.PAID
                : OrderStatus.FAILED;
        orderById.setStatus(orderStatus);
        orderById.setPaymentId(paymentResponse.getPaymentId());
        repository.save(orderById);
        return orderStatus;
    }
}
