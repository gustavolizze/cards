package com.cards.core.domain.card;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardResponse {
    private final UUID id;
    private final String number;
    private final String name;
    private final Integer month;
    private final Integer year;
    private final String securityCode;
    private final String lastFourNumbers;

    public static Mono<CardResponse> ofCard(Card card) {

        return Mono.create(sink -> {
            if (card.getEncrypted() == false) {
                sink.error(new Exception("Card must be encrypted to transform into response!"));
            } else {
                sink.success(new CardResponse(
                        card.getId(),
                        card.getNumber(),
                        card.getName(),
                        card.getMonth(),
                        card.getYear(),
                        card.getSecurityCode(),
                        card.getLastFourNumbers()
                ));
            }
        });
    }
}
