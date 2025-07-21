package br.com.johnnysoft.johnny_rinha.services;

import java.util.concurrent.Flow.Processor;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.johnnysoft.johnny_rinha.enums.ProcessorType;
import br.com.johnnysoft.johnny_rinha.models.Payment;
import br.com.johnnysoft.johnny_rinha.models.ServiceHealthResponse;

@Service
public class PaymentsService {
    private final RestTemplate restTemplate;

    private final RedisService redisService;

    public PaymentsService(RestTemplate restTemplate, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }

    public boolean chooseDefault() {
        ServiceHealthResponse serviceHealthDefault = getServiceHealth(ProcessorType.DEFAULT);
        ServiceHealthResponse serviceHealthFallback = getServiceHealth(ProcessorType.FALLBACK);

        // verificar se o default está disponível
        if (!serviceHealthDefault.failing()) {

            // se sim, verificar se o minResponseTime está alto(definir estratégia)
            if (serviceHealthDefault.minResponseTime() > 200) {

                //// se for alto, verificar se o fallback está disponível
                if (!serviceHealthFallback.failing() && serviceHealthFallback
                        .minResponseTime() < serviceHealthDefault.minResponseTime()) {
                    return false;
                }

                //// se não estiver disponível, true
                return true;
            }

            return true;
        }

        return false;
    }

    public ServiceHealthResponse getServiceHealth(ProcessorType type) {
        ServiceHealthResponse serviceHealth = redisService.findServiceHealth(type);
        if (serviceHealth == null) {
            ServiceHealthResponse response = restTemplate
                    .getForEntity("http://payment-processor-" + type.getValue()
                            + ":8080/payments/service-health", ServiceHealthResponse.class)
                    .getBody();
            redisService.updateHealth("service-health:" + type.getValue(), response);
            return response;
        }
        return serviceHealth;
    }

    public String sendPayment(Payment payment) {
        boolean chooseDefault = chooseDefault();
        String url = "http://payment-processor-default:8080/payments";
        ProcessorType type = ProcessorType.DEFAULT;

        if (!chooseDefault) {
            url = "http://payment-processor-fallback:8080/payments";
            type = ProcessorType.FALLBACK;
        }

        HttpStatusCode statusCode =
                restTemplate.postForEntity(url, payment, String.class).getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            this.redisService.save(payment, type);
            return "OK";
        }
        return "Falha no processamento do pagamento. Status: " + statusCode;
    }
}
