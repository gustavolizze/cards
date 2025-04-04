package com.cards.core.usecases;

import com.cards.core.contracts.UseCase;
import com.cards.core.domain.card.Card;
import com.cards.core.domain.card.CardResponse;
import com.cards.core.domain.card.InsertCard;
import com.cards.core.gateway.CardGateway;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
@Slf4j
public class InsertCardUseCase implements UseCase<InsertCard, Mono<CardResponse>> {

    private final CardGateway cardGateway;


    public InsertCardUseCase(
            CardGateway cardGateway
    ) {
        log.info("[InserCardUseCase] initialized");
        this.cardGateway = cardGateway;
    }

    @Override
    public Mono<CardResponse> execute(InsertCard insertCard) {
        return Card
                .create(insertCard)
                .flatMap(this.cardGateway::create)
                .flatMap(CardResponse::ofCard);
    }
}
