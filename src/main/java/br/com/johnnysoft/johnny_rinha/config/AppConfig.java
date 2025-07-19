package br.com.johnnysoft.johnny_rinha.config;

import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import br.com.johnnysoft.johnny_rinha.models.Payment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisTemplate<UUID, Payment> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<UUID, Payment> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }
}

