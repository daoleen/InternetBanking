package com.daoleen.banking.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by alex on 1/19/15.
 */
@Converter
public class StringToByteArrayConverter implements AttributeConverter<String, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(String attribute) {
        return attribute.getBytes();
    }

    @Override
    public String convertToEntityAttribute(byte[] dbData) {
        return new String(dbData);
    }
}
