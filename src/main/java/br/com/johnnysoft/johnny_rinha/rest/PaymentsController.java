package br.com.johnnysoft.johnny_rinha.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentsController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }
    @GetMapping("/payments-sammary")
    public String paymens() {
        return "Hello Payments";
    }
}
