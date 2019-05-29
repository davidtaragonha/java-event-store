package com.alonso.event.store.example.rest;

import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import com.alonso.event.store.example.DomainPublisher;
import com.alonso.event.store.example.domain.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
//        Car car = Car.replay("Mercedes", "Clase A", 45000);
        domainPublisher.publish(null);
        return Mono.just(null);
    }


    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> getEvents(){
         return Flux.from(eventStore.forSequence(0))
                .subscribeOn(Schedulers.elastic())
                 .publishOn(Schedulers.newSingle("web-subscription"));
    }
}
