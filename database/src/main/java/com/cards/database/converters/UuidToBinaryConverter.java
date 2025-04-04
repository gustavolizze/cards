package com.cards.database.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

@WritingConverter
public class UuidToBinaryConverter implements Converter<UUID, byte[]> {
    @Override
    public byte[] convert(UUID source) {
        var buffer = ByteBuffer.allocate(16);
        buffer.putLong(source.getMostSignificantBits());
        buffer.putLong(source.getLeastSignificantBits());
        return buffer.array();
    }
}
