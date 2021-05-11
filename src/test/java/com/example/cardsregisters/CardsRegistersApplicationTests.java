package com.example.cardsregisters;

import com.example.cardsregisters.dao.CardInt;
import com.example.cardsregisters.dto.Card;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CardsRegistersApplicationTests {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private CardInt cardRepository;

    @Test
    void getAll() {
        Flux<Card> CardFlux = Flux.just(
                Card.builder().active(true).description("mi tarea 1").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 2").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 3").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 4").createAt(new Date()).build(),
                Card.builder().active(true).description("mi tarea 5").createAt(new Date()).build()
        );
        Mockito.when(cardRepository.findAll()).thenReturn(CardFlux);
        webClient.get()
                .uri("/cards")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Card.class)
                .value(cardResponse -> {
                            Assertions.assertThat(cardResponse.size()).isEqualTo(5);
                        }
                );
    }

    @Test
    void getByid() {
        Card card = Card.builder().id(123123123L).description("ABC").createAt(new Date()).build();
        Mono<Card> CardMono = Mono.just(card);
        Mockito.when(cardRepository.findById(123123123L)).thenReturn(CardMono);
        webClient.get()
                .uri("/cards/123123123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Card.class)
                .value(cardResponse -> {
                            Assertions.assertThat(cardResponse.getDescription()).isEqualTo("ABC");
                        }
                );
    }

    @Test
    void saveCard() {
        Card card = Card.builder().id(123123123L).description("ABC").createAt(new Date()).active(true).build();
        Mono<Card> CardMono = Mono.just(Card.builder().description("ABC").createAt(new Date()).active(true).id(123123123L).build());
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(CardMono);
        webClient.post()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(card), Card.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Card.class)
                .value(cardResponse -> {
                            Assertions.assertThat(cardResponse.getDescription()).isEqualTo("ABC");
                        }
                );

    }

    @Test
    void updateCard() {
        Card card = Card.builder().id(123123123L).description("ABC").createAt(new Date()).active(true).build();
        Mono<Card> CardMono = Mono.just(Card.builder().description("ABC").createAt(new Date()).active(true).id(123123123L).build());
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(CardMono);
        webClient.put()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(card), Card.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Card.class)
                .value(cardResponse -> {
                            Assertions.assertThat(cardResponse.getDescription()).isEqualTo("ABC");
                        }
                );

    }

    @Test
    void deleteCard() {
        Mockito.when(cardRepository.deleteById(Mockito.any(Long.class))).thenReturn(Mono.empty());
        webClient.delete()
                .uri("/cards/123123123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

    }

}
