package org.bazar.bazarstore_v2.domain.payment.integration;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.bazar.bazarstore_v2.domain.order.Order;
import org.bazar.bazarstore_v2.domain.order.OrderRequestDto;
import org.bazar.bazarstore_v2.domain.order.OrderResponseDto;
import org.bazar.bazarstore_v2.domain.payment.PaymentProcessingException;
import org.bazar.bazarstore_v2.domain.payment.PaymentRequestDto;
import org.bazar.bazarstore_v2.domain.payment.PaymentResponse;
import org.bazar.bazarstore_v2.domain.payment.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StripePaymentService implements PaymentService {

    @Override
    public PaymentResponse handlePaymentWithMethod(OrderResponseDto orderResponseDto, PaymentRequestDto paymentRequestDto) {
        long amountInCents = convertToCents(orderResponseDto.getAmount());

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd")
                .setPaymentMethod(paymentRequestDto.getPaymentMethodId())
                .setConfirm(true)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build();

        try {
            PaymentIntent intent = PaymentIntent.create(params);
            return new PaymentResponse(intent.getId(), intent.getStatus());
        } catch (StripeException e) {
            String paymentId = null;
            if (e.getStripeError() != null && e.getStripeError().getPaymentIntent() != null) {
                paymentId = e.getStripeError().getPaymentIntent().getId();
            }
            return new PaymentResponse(paymentId, "failed");
        }
    }

    @Override
    public String generatePaymentLink(Order order) {
        try {
            List<SessionCreateParams.LineItem> lineItems = order.getItems().stream()
                    .map(item -> SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount(convertToCents(item.getPrice()))
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(item.getProductName())
                                            .build())
                                    .build())
                            .build())
                    .toList();

            String cancelUrl = "https://yourdomain.com/cancel";
            String successUrl = "https://yourdomain.com/success?session_id={CHECKOUT_SESSION_ID}";
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addAllLineItem(lineItems)
                    //.setCustomer(order.getCustomerId())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new PaymentProcessingException("Failed to create Stripe payment link", e);
        }
    }

    private Long convertToCents(BigDecimal amount) {
        return amount
                .multiply(BigDecimal.valueOf(100)) // Convert dollars to cents
                .setScale(0, RoundingMode.HALF_UP) // Round to nearest whole number
                .longValueExact(); // Fail if precision is lost
    }
}
