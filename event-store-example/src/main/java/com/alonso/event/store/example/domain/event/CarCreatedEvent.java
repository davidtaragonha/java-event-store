package com.alonso.event.store.example.domain.event;

import com.alonso.event.store.core.AggregateIdentifier;
import com.alonso.event.store.core.Payload;
import com.alonso.event.store.core.Revision;
import com.alonso.event.store.example.domain.model.Car;

@Revision(1)
public class CarCreatedEvent extends Payload<Car> {

    @AggregateIdentifier
    private final String id;
    private final String company;
    private final String model;
    private final double prize;

    public CarCreatedEvent(String id, String company, String model, double prize, long version) {
        super(version);
        this.id = id;
        this.company = company;
        this.model = model;
        this.prize = prize;
    }

    public String getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getModel() {
        return model;
    }

    public double getPrize() {
        return prize;
    }
}
