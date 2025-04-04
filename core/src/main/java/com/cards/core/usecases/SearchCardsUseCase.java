package com.cards.core.usecases;

import com.cards.core.contracts.UseCase;
import com.cards.core.domain.card.CardResponse;
import com.cards.core.gateway.CardGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class SearchCardsUseCase implements UseCase<String, Flux<CardResponse>> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final CardGateway cardGateway;

    public SearchCardsUseCase(
            CardGateway cardGateway
    ) {
        this.cardGateway = cardGateway;
    }

    @Override
    public Flux<CardResponse> execute(String number) {
        return this.cardGateway
                .searchByNumber(number)
                .flatMap(CardResponse::ofCard);
    }
}
