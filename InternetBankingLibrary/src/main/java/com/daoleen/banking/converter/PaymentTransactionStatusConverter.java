package com.daoleen.banking.converter;

import com.daoleen.banking.enums.PaymentTransactionStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by alex on 12/23/14.
 */
@Converter
public class PaymentTransactionStatusConverter implements AttributeConverter<PaymentTransactionStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PaymentTransactionStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public PaymentTransactionStatus convertToEntityAttribute(Integer dbData) {
        if(dbData.equals(PaymentTransactionStatus.FILLING_DATA)) {
            return PaymentTransactionStatus.FILLING_DATA;
        }
        if(dbData.equals(PaymentTransactionStatus.PROCESSING)) {
            return PaymentTransactionStatus.PROCESSING;
        }
        return PaymentTransactionStatus.CLOSED;
    }
}
