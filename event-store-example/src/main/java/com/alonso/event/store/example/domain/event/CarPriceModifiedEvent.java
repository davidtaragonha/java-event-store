package com.alonso.event.store.example.domain.event;

import com.alonso.event.store.core.AggregateIdentifier;
import com.alonso.event.store.core.Payload;
import com.alonso.event.store.core.Revision;
import com.alonso.event.store.example.domain.model.Car;

@Revision(1)
public class CarPriceModifiedEvent extends Payload<Car> {

    @AggregateIdentifier
    private final String id;
    private final double price;

    public CarPriceModifiedEvent(String id, double price, long version) {
        super(version);
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
