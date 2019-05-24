package com.alonso.event.store.example.domain.model;

import com.alonso.event.store.example.domain.event.CarCreatedEvent;
import com.alonso.event.store.example.domain.event.CarPriceModifiedEvent;

import java.util.UUID;

public class Car extends Entity {

    private String id;
    private String company;
    private String model;
    private double price;

    private Car(String id, String company, String model, double price) {
        this.id = id;
        this.company = company;
        this.model = model;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public static Car create(String company, String model, double price){
        Car car = new Car(
            UUID.randomUUID().toString(),
            company,
            model,
            price
        );

        car.publish(new CarCreatedEvent(car.getId(), car.getCompany(), car.getModel(), car.getPrice()));
        return car;
    }

    public void changePrice(double price){
        this.price = price;
        publish(new CarPriceModifiedEvent(this.id, this.price));
    }
}
