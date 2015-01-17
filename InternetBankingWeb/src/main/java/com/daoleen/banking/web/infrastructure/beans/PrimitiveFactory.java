package com.daoleen.banking.web.infrastructure.beans;

/**
 * Created by alex on 1/17/15.
 */
public interface PrimitiveFactory {
    public Object getPrimitive(String typeName, String value);
}
