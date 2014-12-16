package com.daoleen.banking.domain;

import java.io.Serializable;

/**
 * Created by alex on 12/10/14.
 */
public abstract class BaseEntity<T extends Serializable> {
    public abstract T getIdentifier();
}
