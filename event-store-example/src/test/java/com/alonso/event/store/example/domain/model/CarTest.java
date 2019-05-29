package com.alonso.event.store.example.domain.model;

import com.alonso.event.store.example.domain.command.CreateCarCommand;
import com.alonso.event.store.example.domain.command.ModifyPriceCommand;
import org.junit.Test;

public class CarTest {

    @Test
    public void create_and_modify_price_of_car() {
        CreateCarCommand createCarCommand = new CreateCarCommand("bmw", "i3", 45000);

        Car car = Car.empty();
        car.handle(createCarCommand);


        ModifyPriceCommand modifyPriceCommand = new ModifyPriceCommand(car.getId(), 78000);
        car.handle(modifyPriceCommand);

        System.out.println(car);
    }

    @Test
    public void create_and_modify_price_with_negativa_value() {
        CreateCarCommand createCarCommand = new CreateCarCommand("bmw", "i3", 45000);

        Car car = Car.empty();
        car.handle(createCarCommand);


        ModifyPriceCommand modifyPriceCommand = new ModifyPriceCommand(car.getId(), -700);
        car.handle(modifyPriceCommand);

        System.out.println(car);
    }
}