package com.daoleen.banking.converter;

import com.daoleen.banking.enums.MoneyReservationStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by alex on 12/22/14.
 */
@Converter(autoApply = true)
public class MoneyReservationStatusConverter implements AttributeConverter<MoneyReservationStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MoneyReservationStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public MoneyReservationStatus convertToEntityAttribute(Integer dbData) {
        MoneyReservationStatus moneyReservationStatus = MoneyReservationStatus.CLOSED;
        moneyReservationStatus.setValue(dbData);
        return moneyReservationStatus;
    }
}
