package com.alonso.event.store.example.infraestructure;

import com.alonso.event.store.core.EventStore;
import com.alonso.event.store.example.domain.model.Car;
import com.alonso.event.store.example.domain.port.CarRepository;

import java.util.Collections;

public class CarRepositoryES implements CarRepository {

    private final EventStore eventStore;

    public CarRepositoryES(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Car findBy(String id) {
        return Car.replay(this.eventStore.findBy(id));
    }

    @Override
    public void save(Car car) {
        eventStore.publish(Collections.emptyList());
    }
}
