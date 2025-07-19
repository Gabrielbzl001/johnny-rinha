package br.com.johnnysoft.johnny_rinha.services;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import br.com.johnnysoft.johnny_rinha.models.Payment;

@Service
public class RedisService {

    private final RedisTemplate<String, Payment> redisTemplate;

    public RedisService(RedisTemplate<String, Payment> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Payment payment) {
        save(payment, "default");
    }

    public void save(Payment payment, String type) {
        double score = payment.requestedAt().atZone(ZoneId.systemDefault()).toEpochSecond();
        ZSetOperations<String, Payment> zset = redisTemplate.opsForZSet();

        zset.add(type, payment, score);
        redisTemplate.opsForValue().set(type + ":" + payment.correlationId().toString(), payment);
    }

    public String find(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public Set<Payment> findPaymentsBetween(Instant start, Instant end, String type) {
        double minScore = start.atZone(ZoneId.systemDefault()).toEpochSecond();
        double maxScore = end.atZone(ZoneId.systemDefault()).toEpochSecond();

        return redisTemplate.opsForZSet()
                .rangeByScore(type, minScore, maxScore);
    }

    public Map<String, Object> getSummary(Instant from, Instant to) {
    Map<String, Object> response = new HashMap<>();
    
    Map<String, Object> defaultData = new HashMap<>();
    Set<Payment> defaultPayments = findPaymentsBetween(from, to, "default");
    double defaultTotal = defaultPayments.stream().mapToDouble(Payment::amount).sum();
    
    defaultData.put("totalRequests", defaultPayments.size());
    defaultData.put("totalAmount", defaultTotal);
    
    Map<String, Object> fallbackData = new HashMap<>();
    Set<Payment> fallbackPayments = findPaymentsBetween(from, to, "fallback");
    double fallbackTotal = fallbackPayments.stream().mapToDouble(Payment::amount).sum();
    
    fallbackData.put("totalRequests", fallbackPayments.size());
    fallbackData.put("totalAmount", fallbackTotal);
    
    response.put("default", defaultData);
    response.put("fallback", fallbackData);
    
    return response;
}
}

