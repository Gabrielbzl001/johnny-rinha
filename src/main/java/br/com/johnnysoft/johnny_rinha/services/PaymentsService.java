package br.com.johnnysoft.johnny_rinha.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.com.johnnysoft.johnny_rinha.models.Payment;


@Service
public class PaymentsService {
    private final RestTemplate restTemplate;

    private final RedisService redisService;

    public PaymentsService(RestTemplate restTemplate, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }

    public String sendPayment(Payment payment) {
        String url = "http://payment-processor-fallback:8080/payments";
        this.redisService.save(payment);
        return restTemplate.postForObject(url, payment, String.class);
    }
}
