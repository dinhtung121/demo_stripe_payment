package com.example.stripe.stripe_backend.controller;

import com.example.stripe.stripe_backend.model.Checkout;
import com.example.stripe.stripe_backend.model.CheckoutPayment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class StripeController {

    private static Gson gson = new Gson();

    private static final String endpointSecret = "whsec_e2dc7554ed55a309f038e8e8ebb658f5d5c2d37dea44aa66b4a458e93ac4a1d8";

    @PostMapping("/payment")
    public String paymentWithCheckoutPage(@RequestBody CheckoutPayment payment) throws StripeException {
        init();
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(payment.getSuccessUrl())
                .setSuccessUrl(payment.getSuccessUrl())
                .setCancelUrl(payment.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(payment.getCurrency()).setUnitAmount(payment.getAmount())
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                        .builder().setName(payment.getName()).build())
                                                .build())
                                .build())
                .build();

        // create a stripe session
        Session session = Session.create(params);
        Map<String, String> responseData = new HashMap<>();
        // We get the sessionId and we putted inside the response data you can get more info from the session object
        responseData.put("id", session.getId());
        // We can return only the sessionId as a String
        return gson.toJson(responseData);

    }

    @PostMapping("/subscription")
    /**
     * Used to create a subscription with strpe checkout page
     * @param checkout
     * @return the subscription id
     * @throws StripeException
     */
    public String subscriptionWithCheckoutPage(@RequestBody Checkout checkout) throws StripeException {
        init();
        SessionCreateParams params = new SessionCreateParams.Builder().setSuccessUrl(checkout.getSuccessUrl())
                .setCancelUrl(checkout.getCancelUrl()).addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L).setPrice(checkout.getPriceId()).build())
                .build();

        try {
            Session session = Session.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return gson.toJson(responseData);
        } catch (Exception e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
        }
    }


    @PostMapping("/webhook")
    public String webhookSession(@RequestBody String requestBody, @RequestHeader(value="Stripe-Signature") String stripeSignature) throws StripeException {
        String payload = requestBody;
        String sigHeader = stripeSignature;
        Event event = null;

        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (JsonSyntaxException e) {
            // Invalid payload
            return "";
        } catch (SignatureVerificationException e) {
            // Invalid signature
            return "";
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        switch (event.getType()) {
            case "payment_intent.succeeded": {
                // Then define and call a function to handle the event payment_intent.succeeded
                System.out.println("Payment success");
                break;
            }
            // ... handle other event types
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return "success";
    }

    private static void init() {
        Stripe.apiKey = "sk_test_51N5POSL2PfBCDjdOIzDrkFBwbPYGyna1zPe2HXozDTnEUBzfiza2kc2CgtmYjWnqdG0HZlIVFIBzRL7jCDhKucNA008YGrODNp";
    }
}
