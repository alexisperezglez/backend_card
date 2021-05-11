package com.example.cardsregisters.services;

import com.example.cardsregisters.dto.DbSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SequenceGeneratorService {

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    public Mono<Long> generateSequence(String seqName) {
        Mono<DbSequence> counter = mongoOperations.findAndModify(Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq",1), FindAndModifyOptions.options().returnNew(true).upsert(true),
                DbSequence.class);
        Mono<Long> seq = counter.flatMap(dbSequence -> {
            return Mono.just(dbSequence.getSeq());
        });
        return seq;
    }
}
