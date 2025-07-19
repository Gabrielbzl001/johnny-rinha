package br.com.johnnysoft.johnny_rinha.rest;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.johnnysoft.johnny_rinha.models.Payment;
import br.com.johnnysoft.johnny_rinha.services.PaymentsService;
import br.com.johnnysoft.johnny_rinha.services.RedisService;

@RestController
public class PaymentsController {
    @Autowired
    PaymentsService paymentsService;
    
    @Autowired
    RedisService redisPaymentService;

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/payments-summary")
    public Map<String, Object> paymentsSummary(@RequestParam("from") Instant from, @RequestParam("to") Instant to) {
        return redisPaymentService.getSummary(from, to);
    }

    @PostMapping("/payments")
    public String payments(@RequestBody Payment payment) {
        return this.paymentsService.sendPayment(payment);
    }
}
