package br.com.johnnysoft.johnny_rinha.services;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.com.johnnysoft.johnny_rinha.models.Payment;

@Service
public class PaymentsService {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String sendPayment(Payment payment) {
        String url = "http://payment-processor-default:8080/payments";
        return restTemplate().postForObject(url, payment, String.class);
    }
}
