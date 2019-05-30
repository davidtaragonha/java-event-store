package com.alonso.event.store.example.domain.model;

import com.alonso.event.store.core.Aggregate;
import com.alonso.event.store.core.Entity;
import com.alonso.event.store.core.Event;
import com.alonso.event.store.example.domain.command.CreateCarCommand;
import com.alonso.event.store.example.domain.command.ModifyPriceCommand;
import com.alonso.event.store.example.domain.event.CarCreatedEvent;
import com.alonso.event.store.example.domain.event.CarPriceModifiedEvent;

import java.util.UUID;
import java.util.stream.Stream;

@Aggregate(version = 1)
public class Car extends Entity {

    private String id;
    private String company;
    private String model;
    private double price;

    private Car(){
    }

    public static Car empty(){
        return new Car();
    }


    public static Car replay(Stream<Event> events){
        Car car = Car.empty();
        events.forEach(event -> car.applyPayload(event.getPayload()));
        return car;
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

    public long getVersion() {
        return version;
    }

    public void handle(ModifyPriceCommand modifyPriceCommand){
        publish(new CarPriceModifiedEvent(
                modifyPriceCommand.getCarId(),
                modifyPriceCommand.getPrice(),
                nextVersion()
        ));
    }

    public void handle(CreateCarCommand createCarCommand){
        publish(
            new CarCreatedEvent(
                UUID.randomUUID().toString(),
                createCarCommand.getCompany(),
                createCarCommand.getModel(),
                createCarCommand.getPrice(),
                nextVersion()
            )
        );
    }

    public void apply(CarCreatedEvent carCreatedEvent){
        this.id = carCreatedEvent.getId();
        this.price = carCreatedEvent.getPrize();
        this.company = carCreatedEvent.getCompany();
        this.model = carCreatedEvent.getModel();
        this.version = carCreatedEvent.getVersion();
    }

    public void apply(CarPriceModifiedEvent carPriceModifiedEvent){
        this.price = carPriceModifiedEvent.getPrice();
        this.version = carPriceModifiedEvent.getVersion();
    }
}
