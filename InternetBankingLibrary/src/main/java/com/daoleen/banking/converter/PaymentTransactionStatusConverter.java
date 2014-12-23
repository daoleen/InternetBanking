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
        if(dbData.equals(PaymentTransactionStatus.OPENED)) {
            return PaymentTransactionStatus.OPENED;
        }
        if(dbData.equals(PaymentTransactionStatus.STEP2)) {
            return PaymentTransactionStatus.STEP2;
        }
        if(dbData.equals(PaymentTransactionStatus.STEP3)) {
            return PaymentTransactionStatus.STEP3;
        }
        return PaymentTransactionStatus.CLOSED;
    }
}
