package br.com.johnnysoft.johnny_rinha.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.com.johnnysoft.johnny_rinha.models.Payment;


@Service
public class PaymentsService {
    private final RestTemplate restTemplate;

    public PaymentsService(RestTemplate restTemplate, RedisPaymentService redisPaymentService) {
        this.restTemplate = restTemplate;
        this.redisPaymentService = redisPaymentService;
    }

    private final RedisPaymentService redisPaymentService;

    public String sendPayment(Payment payment) {
        String url = "http://payment-processor-default:8080/payments";
        redisPaymentService.savePayment(payment);
        return restTemplate.postForObject(url, payment, String.class);
    }
}
