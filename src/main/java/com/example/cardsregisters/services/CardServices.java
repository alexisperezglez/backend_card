package com.example.cardsregisters.services;

import com.example.cardsregisters.dao.CardInt;
import com.example.cardsregisters.dto.Card;
import com.example.cardsregisters.dto.DbSequence;
import com.example.cardsregisters.validators.CustomCardValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;

@Component
@AllArgsConstructor
public class CardServices {
    private final CardInt service;
    private final SequenceGeneratorService generatorService;

    public Mono<ServerResponse> getCards(final ServerRequest request) {
        return ServerResponse.ok().body(this.service.findAll(), Card.class);
    }

    public Mono<ServerResponse> getCardById(final ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("idCard"));
        return ServerResponse.ok().body(service.findById(id), Card.class);
    }

    public Mono<ServerResponse> saveCard(final ServerRequest request) {

        Validator validator = new CustomCardValidator();
        Mono<Card> bodyCard = request
                .bodyToMono(Card.class)
                .map(body -> {
                    Errors errors = new BeanPropertyBindingResult(
                            body,
                            Card.class.getName());
                    validator.validate(body, errors);

                    if (errors == null || errors.getAllErrors().isEmpty()) {
//                        return String.format("Hi, %s [%s]!", body.getName(), body.getCode());
                        return body;
                    } else {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                errors.getAllErrors().toString());
                    }
                });
        /*return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody, String.class);*/



//        Mono<Card> bodyCard = request.bodyToMono(Card.class);
        return bodyCard.flatMap(card -> {
            return generatorService.generateSequence("user_sequence").flatMap(aLong -> {
                if (aLong > 0L) {
                    card.setId(aLong);
                } else {
                    card.setId(1);
                }
                card.setCreateAt(new Date());
                return service.save(card);
            });

        }).flatMap(card -> ServerResponse.ok().body(Mono.just(card), Card.class).switchIfEmpty(ServerResponse.badRequest().body(Mono.empty(), Card.class)));
    }

    public Mono<ServerResponse> updateCard(final ServerRequest request) {
        Mono<Card> cardMono = request.bodyToMono(Card.class);

        return cardMono.flatMap(this.service::save).flatMap(card -> ServerResponse.ok().body(Mono.just(card), Card.class));
    }

    public Mono<ServerResponse> deleteCard(final ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("idCard"));
        return ServerResponse.ok().body(service.deleteById(id), Card.class);
    }

    public Mono<ServerResponse> saveAll(final ServerRequest request) {
        Card card = Card.builder().active(false).createAt(new Date()).description("prueba").build();
        return Mono.just(card)
                .map(cardMono -> {
                    return generatorService.generateSequence("user_sequence").flatMap(aLong -> {
                        if (aLong > 0L) {
                            card.setId(aLong);
                        } else {
                            card.setId(1);
                        }
                        return service.save(cardMono);
                    });
                })
                .flatMap(cardFlux -> ServerResponse.ok().body(this.service.findAll(), Card.class));
    }
}
