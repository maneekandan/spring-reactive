package com.demo.reactivemongo;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.demo.reactivemongo.entity.Device;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@SpringBootApplication(exclude = { RedisReactiveAutoConfiguration.class })
public class ServerSSEApplication {
	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	@Value("classpath:device.json")
	private  Resource resource;
	
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServerSSEApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run();
    }

    @Bean
    public SecurityWebFilterChain sseServerSpringSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
            .anyExchange()
            .permitAll();
        return http.build();
    }
    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() throws JsonParseException, JsonMappingException, IOException {
        System.out.println("hello world, I have just started up");
        
        // insert records one by one every 2 seconds
        		ObjectMapper objectMapper = new ObjectMapper();
        		List<Device> deviceList = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
        		Flux.fromIterable(deviceList)
        				.delayElements(Duration.ofSeconds(2))
        				.flatMap(this.reactiveMongoTemplate::save)
        				.doOnComplete(() -> System.out.println("Complete"))
        				.subscribe();

    }
}
