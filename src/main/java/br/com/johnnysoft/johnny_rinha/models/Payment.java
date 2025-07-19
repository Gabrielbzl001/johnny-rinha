package br.com.johnnysoft.johnny_rinha.models;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Payment")
public record Payment(UUID correlationId, float amount, Instant requestedAt)
        implements Serializable {
}
