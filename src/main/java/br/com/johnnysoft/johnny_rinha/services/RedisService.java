package br.com.johnnysoft.johnny_rinha.services;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import br.com.johnnysoft.johnny_rinha.enums.ProcessorType;
import br.com.johnnysoft.johnny_rinha.models.Payment;
import br.com.johnnysoft.johnny_rinha.models.ServiceHealthResponse;

@Service
public class RedisService {

    private final RedisTemplate<String, Payment> redisTemplate;
    private final RedisTemplate<String, ServiceHealthResponse> healthRedisTemplate;

    public RedisService(RedisTemplate<String, Payment> redisTemplate,
            RedisTemplate<String, ServiceHealthResponse> healthRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.healthRedisTemplate = healthRedisTemplate;
    }

    public void updateHealth(String key, ServiceHealthResponse serviceHealth) {
        healthRedisTemplate.opsForValue().set(key, serviceHealth, Duration.ofSeconds(5));
    }

    public void save(Payment payment, ProcessorType type) {
        double score = payment.requestedAt().atZone(ZoneId.systemDefault()).toEpochSecond();
        ZSetOperations<String, Payment> zset = redisTemplate.opsForZSet();

        zset.add(type.getValue(), payment, score);
        redisTemplate.opsForValue().set(type.getValue() + ":" + payment.correlationId().toString(),
                payment);
    }

    public Set<Payment> findPaymentsBetween(Instant start, Instant end, ProcessorType type) {
        double minScore = start.atZone(ZoneId.systemDefault()).toEpochSecond();
        double maxScore = end.atZone(ZoneId.systemDefault()).toEpochSecond();

        return redisTemplate.opsForZSet().rangeByScore(type.getValue(), minScore, maxScore);
    }

    public String find(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public ServiceHealthResponse findServiceHealth(ProcessorType type) {
        return healthRedisTemplate.opsForValue().get("service-health:" + type.getValue());
    }

    public void purgePayments() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    public Map<String, Object> getSummary(Instant from, Instant to) {
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> defaultData = new HashMap<>();
        Set<Payment> defaultPayments = findPaymentsBetween(from, to, ProcessorType.DEFAULT);
        double defaultTotal = defaultPayments.stream().mapToDouble(Payment::amount).sum();

        defaultData.put("totalRequests", defaultPayments.size());
        defaultData.put("totalAmount", defaultTotal);

        Map<String, Object> fallbackData = new HashMap<>();
        Set<Payment> fallbackPayments = findPaymentsBetween(from, to, ProcessorType.FALLBACK);
        double fallbackTotal = fallbackPayments.stream().mapToDouble(Payment::amount).sum();

        fallbackData.put("totalRequests", fallbackPayments.size());
        fallbackData.put("totalAmount", fallbackTotal);

        response.put("default", defaultData);
        response.put("fallback", fallbackData);

        return response;
    }
}
