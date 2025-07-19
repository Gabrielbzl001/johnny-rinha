package br.com.johnnysoft.johnny_rinha.services;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.johnnysoft.johnny_rinha.models.Payment;

@Service
public class RedisService {

    private RedisTemplate<UUID, Payment> redisTemplate;

    public RedisService(RedisTemplate<UUID, Payment> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Payment payment) {
        redisTemplate.opsForValue().set(payment.correlationId(), payment);
    }

    public String find(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public Map<String, Object> getSummary(Instant from, Instant to) {
        HashMap<String, Object> summary = new HashMap<>();
        int totalPayments = 0;
        double totalAmount = 0.0;

        summary.put("total_payments", totalPayments);
        summary.put("total_amount", totalAmount);
        return summary;
    }
}

