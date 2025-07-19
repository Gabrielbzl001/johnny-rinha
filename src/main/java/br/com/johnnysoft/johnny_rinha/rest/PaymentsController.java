package br.com.johnnysoft.johnny_rinha.rest;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

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
    private final RedisService redisService;

    private final PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService, RedisService redisService) {
        this.paymentsService = paymentsService;
        this.redisService = redisService;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/payments-summary")
    public Map<String, Object> paymentsSummary(@RequestParam("from") Instant from,
            @RequestParam("to") Instant to) {
        return redisService.getSummary(from, to);
    }

    @GetMapping("/payments-between")
    public Set<Payment> findPaymentsBetween(
            @RequestParam Instant start,
            @RequestParam Instant end) {
        return this.redisService.findPaymentsBetween(start, end);
    }

    @PostMapping("/payments")
    public String payments(@RequestBody Payment payment) {
        return this.paymentsService.sendPayment(payment);
    }

}
