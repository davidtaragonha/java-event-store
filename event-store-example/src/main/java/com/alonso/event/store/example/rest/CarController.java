package com.alonso.event.store.example.rest;

import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import com.alonso.event.store.example.domain.command.CreateCarCommand;
import com.alonso.event.store.example.domain.command.ModifyPriceCommand;
import com.alonso.event.store.example.domain.event.CarPriceModifiedEvent;
import com.alonso.event.store.example.domain.model.Car;
import com.alonso.event.store.example.domain.port.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

@RestController()
public class CarController {

    private final CarRepository carRepository;
    private final EventStore eventStore;

    @Autowired
    public CarController(CarRepository carRepository, EventStore eventStore) {
        this.carRepository = carRepository;
        this.eventStore = eventStore;
    }

    @PostMapping("/cars")
    public Mono<Car> create(){
        CreateCarCommand createCarCommand = new CreateCarCommand("mercedes", "claseA", 4000);
        Car car = Car.empty();
        car.handle(createCarCommand);
        carRepository.save(car);

        return Mono.just(car);
    }


    @GetMapping("/cars/{id}")
    @Transactional(readOnly = true)
    public Mono<Car> getCar(@PathVariable String id){
        return Mono.just(carRepository.findBy(id));
    }


    @GetMapping(value = "/cars/events",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> getEvents(){
         return Flux.from(eventStore.forSequence(0))
                .subscribeOn(Schedulers.elastic())
                 .publishOn(Schedulers.newSingle("web-subscription"));
    }
}
