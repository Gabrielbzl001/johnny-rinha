package br.com.johnnysoft.johnny_rinha.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.johnnysoft.johnny_rinha.models.Payment;
import br.com.johnnysoft.johnny_rinha.services.PaymentsService;

@RestController
public class PaymentsController {
    @Autowired
    PaymentsService paymentsService;

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/payments-summary")
    public String paymentsSummary() {
        return "Hello Payments";
    }

    @PostMapping("/payments")
    public String payments(@RequestBody Payment payment) {
        return this.paymentsService.sendPayment(payment);
    }
}
