package org.bazar.bazarstore_v2.facade.controller;

import org.bazar.bazarstore_v2.domain.order.OrderProcessingException;
import org.bazar.bazarstore_v2.domain.payment.PaymentProcessingException;
import org.bazar.bazarstore_v2.domain.payment.PaymentRequestDto;
import org.bazar.bazarstore_v2.facade.OrderCheckoutExperienceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.bazar.bazarstore_v2.common.util.RestUtil.buildResponse;
import static org.bazar.bazarstore_v2.common.util.RestUtil.handleException;

@RestController
@RequestMapping("/api/orders")
public class OrderCheckoutController {

    private final OrderCheckoutExperienceFacade orderCheckoutFacade;

    @Autowired
    public OrderCheckoutController(OrderCheckoutExperienceFacade orderCheckoutFacade) {
        this.orderCheckoutFacade = orderCheckoutFacade;
    }

    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<Map<String, Object>> checkout(@PathVariable String customerId, @RequestBody PaymentRequestDto paymentRequestDto) {
        try {
            orderCheckoutFacade.initiateOrderCheckout(customerId, paymentRequestDto);
            return buildResponse(null, HttpStatus.OK, "order-completed successfully");
        }catch (PaymentProcessingException paymentProcessingException){
            return handleException(paymentProcessingException, HttpStatus.PAYMENT_REQUIRED);
        } catch (OrderProcessingException orderProcessingException) {
            return handleException(orderProcessingException, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
