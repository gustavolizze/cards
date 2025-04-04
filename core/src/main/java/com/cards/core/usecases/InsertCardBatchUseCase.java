package com.cards.core.usecases;

import com.cards.core.contracts.UseCase;
import com.cards.core.domain.card.Card;
import com.cards.core.domain.card.InsertCard;
import com.cards.core.gateway.CardGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InsertCardBatchUseCase implements UseCase<FilePart, Flux<UUID>> {

    private final CardGateway cardGateway;

    public InsertCardBatchUseCase(CardGateway cardGateway) {
        this.cardGateway = cardGateway;
    }

    @Override
    public Flux<UUID> execute(FilePart filePart) {
        return filePart
                .content()
                .reduce(new StringBuilder(), this::parseFile)
                .map(StringBuilder::toString)
                .flatMap(this::parseFromFixedWidth)
                .flatMap(Card::batch)
                .flatMapMany(this.cardGateway::batch);
    }

    private StringBuilder parseFile(StringBuilder builder, DataBuffer buffer) {
        var bytes = new byte[buffer.readableByteCount()];
        buffer.read(bytes);
        builder.append(new String(bytes, StandardCharsets.UTF_8));
        return builder;
    }

    private Mono<List<InsertCard>> parseFromFixedWidth(String content) {
        return Flux.fromIterable(
                Arrays
                        .stream(content.split("\\r?\\n"))
                        .collect(Collectors.toList())
                )
                .flatMap(InsertCard::ofLine)
                .collectList();
    }
}
