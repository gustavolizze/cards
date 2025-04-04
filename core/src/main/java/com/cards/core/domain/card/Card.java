package com.cards.core.domain.card;

import com.cards.core.utils.EncryptUtils;
import com.cards.core.utils.ObjectMapperFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Card {

    private final UUID id;
    private final String number;
    private final String hideNumber;
    private final String name;
    private final Integer month;
    private final Integer year;
    private final String securityCode;
    private final String lastFourNumbers;
    private final Boolean encrypted;
    private final String data;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public SecretKey getKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return EncryptUtils.getKey(this.securityCode);
    }

    public Mono<Card> encrypt() {
        return Mono.create(sink -> {
            try {
                if (this.encrypted == false) {
                    var key = this.getKey();
                    var data = ObjectMapperFactory.mapper().writeValueAsString(this);
                    var encrypted = EncryptUtils.encrypt(data, key);
                    sink.success(Card.encrypt(this, encrypted));
                } else {
                    sink.success(this);
                }
            }
            catch (Exception e) {
                sink.error(e);
            }
        });
    }

    public static Mono<Card> create(InsertCard input) {
        return Mono.just(
                new Card(
                        UUID.randomUUID(),
                        input.getNumber(),
                        input.getNumber(),
                        input.getName(),
                        input.getMonth(),
                        input.getYear(),
                        input.getSecurityCode(),
                        input.lastFourNumbers(),
                        false,
                    null,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
    }

    public static Mono<List<Card>> batch(List<InsertCard> cards) {
        return Flux
                .fromIterable(cards)
                .flatMap(Card::create)
                .collectList();
    }

    public static Mono<Card> fromRawData(
            UUID id,
            String number,
            String name,
            String lastFourNumbers,
            Integer month,
            Integer year,
            String encryptedValue,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return Mono.just(
                new Card(
                        id,
                        number.replaceAll("[^ ]", "*"),
                        number,
                        name,
                        month,
                        year,
                        "***",
                       lastFourNumbers,
                        true,
                        encryptedValue,
                        createdAt,
                        updatedAt
                )
        );
    }

    private static Card encrypt(Card old, String encryptedData) {
        return new Card(
                old.id,
                old.number.replaceAll("[^ ]", "*"),
                old.number,
                old.name,
                old.month,
                old.year,
                old.securityCode.replaceAll("[^ ]", "*"),
                old.lastFourNumbers,
                true,
                encryptedData,
                old.createdAt,
                old.updatedAt
        );
    }
}
