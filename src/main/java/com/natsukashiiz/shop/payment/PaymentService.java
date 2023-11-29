package com.natsukashiiz.shop.payment;

import co.omise.Client;
import co.omise.ClientException;
import co.omise.models.Charge;
import co.omise.models.OmiseException;
import co.omise.requests.Request;
import com.natsukashiiz.shop.common.ApiProperties;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class PaymentService {

    private final PaymentProperties paymentProperties;
    private final ApiProperties apiProperties;

    private Client client() {
        try {
            return new Client.Builder()
                    .publicKey(paymentProperties.pubKey)
                    .secretKey(paymentProperties.secretKey)
                    .build();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    public Charge charge(Double amount, String source, UUID orderId) {
        try {
            Request<Charge> request = new Charge.CreateRequestBuilder()
                    .amount((long) (amount * 100))
                    .currency("thb")
                    .source(source)
                    .returnUri(apiProperties.getPaymentReturn() + orderId)
                    .metadata("orderId", orderId)
                    .build();
            return client().sendRequest(request);
        } catch (IOException | OmiseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
