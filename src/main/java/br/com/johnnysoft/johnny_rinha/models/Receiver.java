package br.com.johnnysoft.johnny_rinha.models;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.johnnysoft.johnny_rinha.services.PaymentsService;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private final PaymentsService paymentService;

    private AtomicInteger counter = new AtomicInteger();

    private final ObjectMapper objectMapper;

    public Receiver(ObjectMapper objectMapper, PaymentsService paymentService) {
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
        this.objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void receiveMessage(String message) {
        try {
            Payment payment = objectMapper.readValue(message, Payment.class);

            paymentService.sendPayment(payment);
            LOGGER.info("Received <{}>", payment);
            counter.incrementAndGet();

        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to deserialize payment: {}", message, e);
        }
        counter.incrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }
}