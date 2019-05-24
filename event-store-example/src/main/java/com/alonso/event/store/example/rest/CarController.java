package com.alonso.event.store.example.rest;

import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import com.alonso.event.store.example.DomainPublisher;
import com.alonso.event.store.example.domain.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController("/cars")
public class CarController {

    private final DomainPublisher domainPublisher;
    private final EventStore eventStore;

    @Autowired
    public CarController(DomainPublisher domainPublisher, EventStore eventStore) {
        this.domainPublisher = domainPublisher;
        this.eventStore = eventStore;
    }

    @PostMapping
    Mono<Car> create(){
        Car car = Car.create("Mercedes", "Clase A", 45000);
        domainPublisher.publish(car);
        return Mono.just(car);
    }

    @GetMapping
    Flux<Event> getEvents(){
        return Flux.empty();
    }
}
