package com.alonso.event.store.example.domain.port;

import com.alonso.event.store.example.domain.model.Car;

public interface CarRepository {
    Car findBy(String id);
    void save(Car car);
}
