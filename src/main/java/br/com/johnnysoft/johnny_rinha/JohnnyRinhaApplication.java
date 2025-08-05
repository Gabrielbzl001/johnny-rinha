package br.com.johnnysoft.johnny_rinha;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import br.com.johnnysoft.johnny_rinha.models.Payment;
import br.com.johnnysoft.johnny_rinha.models.Receiver;

@SpringBootApplication
public class JohnnyRinhaApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(JohnnyRinhaApplication.class);

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = SpringApplication.run(JohnnyRinhaApplication.class, args);

        RedisTemplate<String, Payment> template = ctx.getBean("redisTemplate", RedisTemplate.class);
        Receiver receiver = ctx.getBean(Receiver.class);

        while (receiver.getCount() == 0) {

            Payment payment = new Payment(new UUID(1, 0), 1, Instant.now());
            LOGGER.error(payment.toString());
            LOGGER.info("Sending message...");
            LOGGER.info(Long.toString(template.convertAndSend("chat", payment)));
            Thread.sleep(500L);
        }

        System.exit(0);
    }

}
