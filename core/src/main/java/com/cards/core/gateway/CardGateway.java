package com.cards.core.gateway;

import com.cards.core.domain.card.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CardGateway {
    Mono<Card> create(Card card);
    Flux<Card> searchByNumber(String number);
    Flux<UUID> batch(List<Card> cards);
}
