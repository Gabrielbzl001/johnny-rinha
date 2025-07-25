package br.com.johnnysoft.johnny_rinha.models;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonFormat;

@RedisHash("Payment")
public record Payment(UUID correlationId, float amount,
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC") Instant requestedAt)
                implements Serializable {
        public Payment {
                requestedAt = requestedAt != null ? requestedAt : Instant.now();
        }
}