package com.daoleen.banking.web.infrastructure.beans.impl;

import com.daoleen.banking.web.infrastructure.beans.PrimitiveFactory;

import javax.inject.Singleton;

/**
 * Created by alex on 1/17/15.
 */
@Singleton
public class PrimitiveFactoryImpl implements PrimitiveFactory {
    @Override
    public Object getPrimitive(String typeName, String value) {
        switch (typeName) {
            case "int":
                return Integer.parseInt(value);

            case "double":
                return Double.parseDouble(value);

            case "float":
                return Float.parseFloat(value);

            case "boolean":
                return Boolean.parseBoolean(value);

            default:
                return value;
        }
    }
}
