package br.com.johnnysoft.johnny_rinha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import br.com.johnnysoft.johnny_rinha.models.Payment;
import br.com.johnnysoft.johnny_rinha.models.ServiceHealthResponse;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisTemplate<String, ServiceHealthResponse> healthRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ServiceHealthResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, Payment> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Payment> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }
}
