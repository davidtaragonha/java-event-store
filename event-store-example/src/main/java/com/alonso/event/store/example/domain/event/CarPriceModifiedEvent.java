package com.alonso.event.store.example.domain.event;

import com.alonso.event.store.core.AggregateClass;
import com.alonso.event.store.core.AggregateIdentifier;
import com.alonso.event.store.core.Payload;
import com.alonso.event.store.example.domain.model.Car;

@AggregateClass(Car.class)
public class CarPriceModifiedEvent extends Payload {

    @AggregateIdentifier
    private final String id;
    private final double price;

    public CarPriceModifiedEvent(String id, double price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
}
