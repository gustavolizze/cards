package com.cards.database.config;

import com.cards.database.converters.BinaryToUuidConverter;
import com.cards.database.converters.UuidToBinaryConverter;
import io.r2dbc.spi.ConnectionFactory;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.ArrayList;


@EnableConfigurationProperties({ R2dbcProperties.class, FlywayProperties.class })
@EnableR2dbcRepositories(
        basePackages = { "com.cards.database.repositories" }
)
@Configuration
public class DatabaseConfig {



    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        var converters = new ArrayList<>();
        converters.add(new BinaryToUuidConverter());
        converters.add(new UuidToBinaryConverter());
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, converters);
    }

}
