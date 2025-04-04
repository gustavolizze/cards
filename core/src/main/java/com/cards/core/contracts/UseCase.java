package com.cards.core.contracts;

public interface UseCase<Input, Result> {
    Result execute(Input input);
}
