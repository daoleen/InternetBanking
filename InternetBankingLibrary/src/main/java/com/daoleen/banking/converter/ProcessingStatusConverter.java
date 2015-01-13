package com.daoleen.banking.converter;

import com.daoleen.banking.enums.ProcessingStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by alex on 1/12/15.
 */
@Converter
public class ProcessingStatusConverter implements AttributeConverter<ProcessingStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProcessingStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public ProcessingStatus convertToEntityAttribute(Integer dbData) {
        if(dbData.equals(ProcessingStatus.FAIL)) {
            return ProcessingStatus.FAIL;
        }

        if(dbData.equals(ProcessingStatus.SUCCESS)) {
            return ProcessingStatus.SUCCESS;
        }

        return ProcessingStatus.NOT_READY;
    }
}
