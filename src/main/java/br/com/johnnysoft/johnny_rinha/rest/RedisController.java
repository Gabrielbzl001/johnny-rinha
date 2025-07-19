package br.com.johnnysoft.johnny_rinha.rest;

import br.com.johnnysoft.johnny_rinha.services.RedisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/{key}")
    public void save(@PathVariable String key, @RequestBody String value) {
        redisService.save(key, value);
    }

    @GetMapping("/{key}")
    public String buscar(@PathVariable String key) {
        return redisService.find(key);
    }

    @DeleteMapping("/{key}")
    public void delete(@PathVariable String key) {
        redisService.delete(key);
    }
}
