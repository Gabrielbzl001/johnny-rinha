package br.com.johnnysoft.johnny_rinha.services;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        double score = payment.requestedAt().atZone(ZoneId.systemDefault()).toEpochSecond();
        ZSetOperations<String, Payment> zset = redisTemplate.opsForZSet();

        zset.add("payment", payment, score);
        redisTemplate.opsForValue().set("payment:" + payment.correlationId().toString(), payment);
    }

    public String find(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public String findPaymentsBetween(Instant start, Instant end) {
        double minScore = start.atZone(ZoneId.systemDefault()).toEpochSecond();
        double maxScore = end.atZone(ZoneId.systemDefault()).toEpochSecond();

        return redisTemplate.opsForZSet()
                .rangeByScore("payment", minScore, maxScore).toString();
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

