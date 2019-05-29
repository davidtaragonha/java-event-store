package com.alonso.event.store.example.domain.command;

public class CreateCarCommand implements Command {
    private final String company;
    private final String model;
    private final double price;

    public CreateCarCommand(String company, String model, double price) {
        this.company = company;
        this.model = model;
        this.price = price;
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
}
