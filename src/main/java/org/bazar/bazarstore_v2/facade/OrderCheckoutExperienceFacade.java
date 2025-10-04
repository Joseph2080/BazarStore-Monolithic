package org.bazar.bazarstore_v2.facade;

import org.bazar.bazarstore_v2.domain.cart.Cart;
import org.bazar.bazarstore_v2.domain.cart.CartNotFoundException;
import org.bazar.bazarstore_v2.domain.cart.CartService;
import org.bazar.bazarstore_v2.domain.cart.CartUtil;
import org.bazar.bazarstore_v2.domain.notification.NotificationRequestDto;
import org.bazar.bazarstore_v2.domain.notification.NotificationType;
import org.bazar.bazarstore_v2.domain.notification.factory.NotificationDispatcherFactory;
import org.bazar.bazarstore_v2.domain.order.OrderProcessingException;
import org.bazar.bazarstore_v2.domain.order.OrderRequestDto;
import org.bazar.bazarstore_v2.domain.order.OrderResponseDto;
import org.bazar.bazarstore_v2.domain.order.OrderService;
import org.bazar.bazarstore_v2.domain.order.OrderStatus;
import org.bazar.bazarstore_v2.domain.payment.PaymentProcessingException;
import org.bazar.bazarstore_v2.domain.payment.PaymentRequestDto;
import org.bazar.bazarstore_v2.domain.payment.PaymentResponse;
import org.bazar.bazarstore_v2.domain.payment.PaymentService;
import org.bazar.bazarstore_v2.domain.payment.PaymentStatus;
import org.bazar.bazarstore_v2.domain.product.ProductService;
import org.bazar.bazarstore_v2.domain.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.bazar.bazarstore_v2.common.util.AsyncTaskExecutor.runTasksNonBlocking;


@Component
public class OrderCheckoutExperienceFacade {
    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final NotificationDispatcherFactory notificationDispatcherFactory;
    private static final Logger logger = LoggerFactory.getLogger(OrderCheckoutExperienceFacade.class);
    private final UserService userService;

    @Autowired
    public OrderCheckoutExperienceFacade(OrderService orderService,
                                         CartService cartService,
                                         PaymentService paymentService,
                                         ProductService productService,
                                         NotificationDispatcherFactory notificationDispatcherFactory,
                                         UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.productService = productService;
        this.notificationDispatcherFactory = notificationDispatcherFactory;
        this.userService = userService;
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

            //handle paymentResponse here as well.
            handlePaymentOutcome(orderResponseDto, paymentResponse);
            //update the product stock after successful order placement
            //these functions could be asynchronous and ensure that I create a utility for concurrent operations as well
            // async stock updates
            runTasksNonBlocking(List.of(
                    () -> updateProductStock(cart),
                    () -> sendNotification(orderResponseDto),
                    () -> cartService.clearById(CartUtil.generateCartKey(cart.getUuid()))
            ));
        } catch (CartNotFoundException e) {
            throw new OrderProcessingException("unable to find a cart to initiate order", e);
        } catch (Exception e) {
            logger.error("Order checkout failed for customerId {}: {}", customerId, e.getMessage(), e);
            throw new OrderProcessingException("Order checkout failed: " + e.getMessage(), e);
        }
    }

    private void updateProductStock(Cart cart) {
        cart.getItems().parallelStream()
                .forEach(item -> productService.updateProductStock(item.getProductId(), item.getQuantity()));
    }

    private void handlePaymentOutcome(OrderResponseDto orderResponseDto, PaymentResponse paymentResponse) {
        PaymentStatus paymentStatus = paymentResponse.getStatus();
        String paymentId = paymentResponse.getPaymentId();

        OrderStatus orderStatus = orderService.updateOrderStatusByOrderId(orderResponseDto.getOrderId(), paymentResponse);

        if (paymentResponse.getStatus() != PaymentStatus.SUCCEEDED) {
            logger.warn("Order {} failed. Payment ID: {}, Status: {}", orderResponseDto.getOrderId(), paymentId, paymentStatus);
            throw new PaymentProcessingException("Payment failed with status: " + paymentStatus);
        }
        logger.info("Order {} updated successfully. Status: {}, Payment ID: {}", orderStatus, orderResponseDto.getOrderId(), paymentStatus);
    }

    private void sendNotification(OrderResponseDto orderResponseDto) {
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .subject("Order Confirmation")
                .type(NotificationType.EMAIL)
                .recipient(userService.findUserById(orderResponseDto.getCustomerId()).getEmail()) // fetch from user details by customerId/uuid
                .message("Your order has been placed successfully and the total bill is : " + orderResponseDto.getTotalAmount())
                .build();

        notificationDispatcherFactory.send(notificationRequestDto);

    }
}