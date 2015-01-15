package com.daoleen.banking.converter;

import com.daoleen.banking.enums.MoneyReservationStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by alex on 12/22/14.
 */
@Converter
public class MoneyReservationStatusConverter implements AttributeConverter<MoneyReservationStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MoneyReservationStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public MoneyReservationStatus convertToEntityAttribute(Integer dbData) {
        if(dbData.intValue() == MoneyReservationStatus.OPENED.getValue()) {
            return MoneyReservationStatus.OPENED;
        }
        return MoneyReservationStatus.CLOSED;
    }
}
