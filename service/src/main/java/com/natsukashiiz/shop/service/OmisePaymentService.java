package com.natsukashiiz.shop.service;

import co.omise.Client;
import co.omise.ClientException;
import co.omise.models.Charge;
import co.omise.models.OmiseException;
import co.omise.requests.Request;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.UUID;

public class OmisePaymentService {

    private final String omisePubKey;
    private final String omiseSecretKey;
    private final String paymentReturnUrl;

    public OmisePaymentService(String omisePubKey, String omiseSecretKey, String paymentReturnUrl) {
        this.omisePubKey = omisePubKey;
        this.omiseSecretKey = omiseSecretKey;
        this.paymentReturnUrl = paymentReturnUrl;
    }


    private Client client() {
        try {
            return new Client.Builder()
                    .publicKey(omisePubKey)
                    .secretKey(omiseSecretKey)
                    .build();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    public Charge charge(Double amount, String source, UUID orderId, Long expires) throws IOException, OmiseException {
        Request<Charge> request = new Charge.CreateRequestBuilder()
                .amount((long) (amount * 100))
                .currency("thb")
                .source(source)
                .returnUri(paymentReturnUrl + orderId)
                .metadata("orderId", orderId)
                .expiresAt(new DateTime(expires))
                .build();
        return client().sendRequest(request);
    }
}
