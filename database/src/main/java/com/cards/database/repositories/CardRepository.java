package com.cards.database.repositories;

import com.cards.database.models.CardRow;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CardRepository extends ReactiveCrudRepository<CardRow, UUID> {
    Flux<CardRow> findByNumberStartingWithIgnoreCase(String number);

    @Modifying
    @Query("""
    INSERT INTO cards (id, number, name, month, year, security_code, last_four_numbers, data, created_at, updated_at)
    VALUES (:#{#cardRow.id}, :#{#cardRow.number}, :#{#cardRow.name}, :#{#cardRow.month}, :#{#cardRow.year}, 
            :#{#cardRow.securityCode}, :#{#cardRow.lastFourNumbers}, :#{#cardRow.data}, 
            :#{#cardRow.createdAt}, :#{#cardRow.updatedAt})
    ON DUPLICATE KEY UPDATE 
        name = VALUES(name), 
        month = VALUES(month), 
        year = VALUES(year), 
        security_code = VALUES(security_code), 
        last_four_numbers = VALUES(last_four_numbers), 
        data = VALUES(data), 
        updated_at = VALUES(updated_at)
""")
    Mono<CardRow> create(CardRow cardRow);
}
