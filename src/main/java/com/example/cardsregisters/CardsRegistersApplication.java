package com.example.cardsregisters;

import com.example.cardsregisters.dao.CardInt;
import com.example.cardsregisters.dto.Card;
import com.example.cardsregisters.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Date;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class CardsRegistersApplication implements CommandLineRunner, WebFluxConfigurer {

    @Autowired
    private CardInt service;
    @Autowired
    private SequenceGeneratorService generatorService;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(CardsRegistersApplication.class, args);
    }

    // Initialize data
    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("card").subscribe();
        Flux.just(
                Card.builder().active(true).description("mi tarea 1").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 2").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 3").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 4").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 5").createAt(new Date()).build()
        ).flatMap(card -> {
            return generatorService.generateSequence("user_sequence").flatMap(aLong -> {
                if (aLong > 0L) {
                    card.setId(aLong);
                } else {
                    card.setId(1L);
                }
                return service.save(card);
            });
        }).subscribe();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("PUT", "POST", "GET", "DELETE")
                .maxAge(3600);
    }
}
