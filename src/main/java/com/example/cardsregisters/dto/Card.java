package com.example.cardsregisters.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "card")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Card {

    @Transient
    public static final String SEQUENCE_NAME="user_sequence";

    @Id private long id;
    private String description;
    private Date createAt;
    private boolean active;
}
