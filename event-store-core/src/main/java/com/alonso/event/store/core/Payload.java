package com.alonso.event.store.core;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public abstract class Payload {

    @Transient
    public final Class getAggregate(){
        AggregateClass annotation = this.getClass().getAnnotation(AggregateClass.class);
        requireNonNull(annotation, "Missing aggregate annotation in the payload class");
        return annotation.value();
    }

    @Transient
    public final String getAggregateId(){
        Field field = Arrays.stream(
            this.getClass().getDeclaredFields()
        )
        .filter(f -> f.isAnnotationPresent(AggregateIdentifier.class))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Missing aggregateIdentifier annotation in the payload class"));

        try {
            field.setAccessible(true);
            String id = (String)field.get(this);
            field.setAccessible(false);
            return id;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Transient
    public final String getEventType(){return this.getClass().getSimpleName();}
}
