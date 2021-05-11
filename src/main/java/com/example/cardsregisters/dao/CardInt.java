package com.example.cardsregisters.dao;

import com.example.cardsregisters.dto.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CardInt extends ReactiveMongoRepository<Card, Long> {



}
