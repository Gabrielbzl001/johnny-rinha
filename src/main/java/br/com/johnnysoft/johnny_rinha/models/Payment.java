package br.com.johnnysoft.johnny_rinha.models;

import java.time.Instant;
import java.util.UUID;


public record Payment(UUID id, float amount, Instant createdAt) {
}
