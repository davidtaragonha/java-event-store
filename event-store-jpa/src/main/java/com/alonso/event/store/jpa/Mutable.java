package com.alonso.event.store.jpa;

import static java.util.Objects.requireNonNull;

public class Mutable<T> {

    private T value;

    public Mutable(T value) {
        setValue(value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        requireNonNull(value, "Can not be null the value for mutable object");
        this.value = value;
    }
}
