package com.alonso.event.store.core;

import java.lang.reflect.Field;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public abstract class Payload {

    public final Class getAggregate(){
        AggregateClass annotation = this.getClass().getAnnotation(AggregateClass.class);
        requireNonNull(annotation, "Missing aggregate annotation in the payload class");
        return annotation.value();
    }

    public final String getAggregateId(){
        Field field = Arrays.stream(
            this.getClass().getFields()
        )
        .filter(f -> f.isAnnotationPresent(AggregateIdentifier.class))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Missing aggregateIdentifier annotation in the payload class"));

        try {
            return (String)field.get(this);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public final String getEventType(){return this.getClass().getSimpleName();}
}
