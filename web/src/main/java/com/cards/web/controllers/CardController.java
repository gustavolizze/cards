package com.cards.web.controllers;

import com.cards.core.domain.card.CardResponse;
import com.cards.core.domain.card.InsertCard;
import com.cards.core.usecases.InsertCardBatchUseCase;
import com.cards.core.usecases.InsertCardUseCase;
import com.cards.core.usecases.SearchCardsUseCase;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final SearchCardsUseCase searchCardsUseCase;
    private final InsertCardUseCase insertCardUseCase;
    private final InsertCardBatchUseCase insertCardBatchUseCase;

    public CardController(
            SearchCardsUseCase searchCardsUseCase,
            InsertCardUseCase insertCardUseCase,
            InsertCardBatchUseCase insertCardBatchUseCase

    ) {
        this.searchCardsUseCase = searchCardsUseCase;
        this.insertCardUseCase = insertCardUseCase;
        this.insertCardBatchUseCase = insertCardBatchUseCase;
    }

    @GetMapping
    public Flux<CardResponse> get(
            @RequestParam(required = false) String number
    ) {
        return this.searchCardsUseCase.execute(number);
    }

    @PostMapping
    public Mono<CardResponse> create(
          @Valid @RequestBody InsertCard input
    ) {
        return this.insertCardUseCase.execute(input);
    }

    @RequestMapping("/batch")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Flux<UUID> batch(
            @RequestPart("file") FilePart filePart
    ) {
        if (filePart.filename().endsWith(".txt") == false) {
            return Flux.error(new Exception("Only text files are allowed!!!"));
        }

        return this.insertCardBatchUseCase.execute(filePart);
    }

}
