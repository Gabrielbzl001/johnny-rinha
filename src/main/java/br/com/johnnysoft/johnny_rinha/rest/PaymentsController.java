package br.com.johnnysoft.johnny_rinha.rest;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.johnnysoft.johnny_rinha.enums.ProcessorType;
import br.com.johnnysoft.johnny_rinha.models.Payment;
import br.com.johnnysoft.johnny_rinha.services.PaymentsService;
import br.com.johnnysoft.johnny_rinha.services.RedisService;

@RestController
public class PaymentsController {
    private final RedisService redisService;
    private final RedisTemplate<String, Payment> redisTemplate;

    public PaymentsController(RedisService redisService,
            RedisTemplate<String, Payment> redisTemplate) {
        this.redisService = redisService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/payments-summary")
    public Map<String, Object> paymentsSummary(
            @RequestParam(value = "from", required = false) Instant from,
            @RequestParam(value = "to", required = false) Instant to) {

        if (from == null || to == null) {
            return redisService.getSummary(Instant.parse("1980-01-01T00:00:00Z"), Instant.now());
        }

        return redisService.getSummary(from, to);
    }

    @GetMapping("/payments-between")
    public Set<Payment> findPaymentsBetween(@RequestParam Instant start,
            @RequestParam Instant end) {
        return this.redisService.findPaymentsBetween(start, end, ProcessorType.DEFAULT);
    }

    @PostMapping("/payments")
    public String payments(@RequestBody Payment payment) {
        redisTemplate.convertAndSend("chat", payment);
        return "ok";
    }

    @PostMapping("/purge-payments")
    public String purgePayments() {
        this.redisService.purgePayments();
        return "ok";
    }

}
