package com.daoleen.banking.domain;

import java.io.Serializable;

/**
 * Created by alex on 12/16/14.
 *
 * @type T type of entity identifier
 */
public interface Identifiable<T extends Serializable> {
    public T getId();
}
