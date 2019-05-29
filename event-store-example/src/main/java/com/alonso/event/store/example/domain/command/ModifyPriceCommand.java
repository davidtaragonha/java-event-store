package com.alonso.event.store.example.domain.command;

public class ModifyPriceCommand implements Command {
    private final String carId;
    private final double price;

    public ModifyPriceCommand(String carId, double price) {
        this.carId = carId;
        this.price = price;
    }

    public String getCarId() {
        return carId;
    }

    public double getPrice() {
        return price;
    }
}
