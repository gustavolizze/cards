package com.cards.core.domain.card;

import jakarta.validation.Validation;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import org.springframework.validation.FieldError;


import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertCard {
    @Pattern(regexp = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|6(?:011|5[0-9]{2})[0-9]{12}|(?:2131|1800|35\\\\d{3})\\\\d{11})$", message = "Please enter a valid card number")
    private String number;

    @NotNull(message = "Name must contain at least two words with only letters!")
    @NotBlank(message = "Name must contain at least two words with only letters!")
    @Pattern(regexp = "^.* [A-Za-z]+$", message = "Name must contain at least two words with only letters!")
    private String name;

    @NotNull(message = "Please inform the month")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Please inform the year")
    @Min(value = 2025, message = "Year must be the current year or after")
    private Integer year;

    @NotNull(message = "Please inform security code")
    @Length(min = 3, max = 3, message = "Security code must be 3 characters")
    private String securityCode;

    public String lastFourNumbers() {
        return this.number.substring(this.number.length() - 4, this.number.length());
    }


    public static Mono<InsertCard> ofLine(String line) {
        return Mono.create(sink -> {
            try {
                var factory = Validation.buildDefaultValidatorFactory();
                var validator = factory.getValidator();
                var number = line.substring(0, 16).trim();
                var name = line.substring(16, 46).trim();
                var month = Integer.valueOf(line.substring(46, 47).trim());
                var year = Integer.valueOf(line.substring(47, 51).trim());
                var securityCode = line.substring(51, 54).trim();
                var card = new InsertCard(number, name, month, year, securityCode);
                var validations = validator.validate(card);

                if (validations.isEmpty()) {
                    sink.success(card);
                } else {
                    var bindingResult = new BeanPropertyBindingResult(card, "InsertCard");

                    validations.forEach(
                            src ->
                                    bindingResult
                                            .addError(
                                                    new FieldError(
                                                            "InsertCard",
                                                            src.getPropertyPath().toString(),
                                                            src.getMessage())
                                                    )
                    );

                    sink.error(
                            new WebExchangeBindException(
                                    new MethodParameter(InsertCard.class.getDeclaredMethod("ofLine"), 0),
                                    bindingResult
                            )
                    );
                }
            } catch (Exception e) {
                sink.error(e);
            }
        });
    }
}
