package com.cards.database.models;

import com.cards.core.domain.card.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("cards")
public class CardRow {
    private UUID id;
    private String number;
    private String name;
    private Integer month;
    private Integer year;
    private String securityCode;
    private String lastFourNumbers;
    private String data;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Transient
    private Boolean isNew;

    public Mono<Card> toDomain() {
        return Card.fromRawData(
                this.id,
                this.number,
                this.name,
                this.lastFourNumbers,
                this.month,
                this.year,
                this.data,
                this.createdAt,
                this.updatedAt
        );
    }

    public static Mono<CardRow> ofNewCard(Card card) {
        return card
                .encrypt()
                .map(src ->
                        new CardRow(
                                src.getId(),
                                src.getHideNumber(),
                                src.getName(),
                                src.getMonth(),
                                src.getYear(),
                                src.getSecurityCode(),
                                src.getLastFourNumbers(),
                                src.getData(),
                                src.getCreatedAt(),
                                src.getUpdatedAt(),
                                true
                        )
                );
    }

}



