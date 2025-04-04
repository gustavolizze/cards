package com.cards.database.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

@ReadingConverter
public class BinaryToUuidConverter implements Converter<byte[], UUID> {
    @Override
    public UUID convert(byte[] source) {
        var buffer = ByteBuffer.wrap(source);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
