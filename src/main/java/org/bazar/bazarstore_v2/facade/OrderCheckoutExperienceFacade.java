package org.bazar.bazarstore_v2.facade;

import org.bazar.bazarstore_v2.domain.cart.Cart;
import org.bazar.bazarstore_v2.domain.cart.CartNotFoundException;
import org.bazar.bazarstore_v2.domain.cart.CartService;
import org.bazar.bazarstore_v2.domain.order.OrderProcessingException;
import org.bazar.bazarstore_v2.domain.order.OrderRequestDto;
import org.bazar.bazarstore_v2.domain.order.OrderResponseDto;
import org.bazar.bazarstore_v2.domain.order.OrderService;
import org.bazar.bazarstore_v2.domain.payment.PaymentProcessingException;
import org.bazar.bazarstore_v2.domain.payment.PaymentRequestDto;
import org.bazar.bazarstore_v2.domain.payment.PaymentResponse;
import org.bazar.bazarstore_v2.domain.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderCheckoutExperienceFacade {
    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(OrderCheckoutExperienceFacade.class);

    public OrderCheckoutExperienceFacade(OrderService orderService,
                                         CartService cartService,
                                         PaymentService paymentService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    public void initiateOrderCheckout(String customerId, PaymentRequestDto paymentRequestDto) {
        try {
            Cart cart = cartService.findCartByCustomerId(customerId);
            logger.debug("Retrieved cart for customerId {}: {}", customerId, cart);
            OrderRequestDto orderRequestDto = new CartToOrderRequestAdapter().adapt(cart);
            logger.debug("Order initiated successfully cart to order: {}", orderRequestDto);
            OrderResponseDto orderResponseDto = orderService.create(orderRequestDto);

            logger.debug("Order has been initiated and the current status is : {}", orderResponseDto);

            PaymentResponse paymentResponse = paymentService.handlePaymentWithMethod(orderResponseDto, paymentRequestDto);
            logger.info("Received payment response: {}", paymentResponse);

            handlePaymentOutcome(orderResponseDto, paymentResponse);
        } catch (CartNotFoundException e) {
            throw new OrderProcessingException("unable to find a cart to initiate order", e);
        } catch (Exception e) {
            logger.error("Order checkout failed for customerId {}: {}", customerId, e.getMessage(), e);
            throw e;
        }
    }

    private void handlePaymentOutcome(OrderResponseDto orderResponseDto, PaymentResponse paymentResponse) {
        String status = paymentResponse.getStatus();
        String paymentId = paymentResponse.getPaymentId();

        if ("succeeded".equalsIgnoreCase(status)) {
            orderService.updateOrderStatusByOrderId(orderResponseDto.getOrderId(), paymentId);
            logger.info("Order succeeded.");
        } else {
            orderService.updateOrderStatusByOrderId(orderResponseDto.getOrderId(), status);
            logger.warn("Order failed with payment status '{}'. Payment ID: {}, Order: {}",
                    status,
                    paymentId,
                    orderResponseDto.getOrderId());
            throw new PaymentProcessingException("Payment failed with status: " + status);
        }
    }
}