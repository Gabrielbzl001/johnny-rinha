package br.com.johnnysoft.johnny_rinha.models;

import java.io.Serializable;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("ServiceHealth")
public record ServiceHealthResponse(boolean failing, int minResponseTime) implements Serializable {
}
