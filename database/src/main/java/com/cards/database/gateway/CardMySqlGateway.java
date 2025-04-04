package com.cards.database.gateway;

import com.cards.core.domain.card.Card;
import com.cards.core.gateway.CardGateway;
import com.cards.database.models.CardRow;
import com.cards.database.repositories.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class CardMySqlGateway implements CardGateway {

    private final CardRepository cardRepository;

    public CardMySqlGateway(
            CardRepository cardRepository
    ) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Mono<Card> create(Card card) {
        return CardRow
                .ofNewCard(card)
                .flatMap(this.cardRepository::save)
                .flatMap(row -> row.toDomain());
    }

    @Override
    public Flux<Card> searchByNumber(String number) {
        return this.cardRepository
                .findByNumberStartingWithIgnoreCase(number == null ? "" : number)
                .flatMap(row -> row.toDomain());
    }

    @Override
    public Flux<UUID> batch(List<Card> cards) {
        return Flux.fromIterable(cards)
                .flatMap(CardRow::ofNewCard)
                .flatMap(this.cardRepository::save)
                .map(row -> row.getId());
    }

}
