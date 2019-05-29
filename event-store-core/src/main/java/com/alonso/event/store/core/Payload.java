package com.alonso.event.store.core;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public abstract class Payload<T extends Entity>{
    protected long version;

    protected Payload(long version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public final Class<T> getAggregate(){
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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

    @Transient
    public int getAggregateVersion() {
        Aggregate annotation = getAggregate().getAnnotation(Aggregate.class);
        requireNonNull(annotation, "Missing aggregate annotation in the Entity class");
        return annotation.version();
    }
}
