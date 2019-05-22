package com.alonso.event.store.example.rest;

import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import com.alonso.event.store.example.domain.event.CarCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("cars")
public class CarController {
    private final EventStore eventStore;

    @Autowired
    public CarController(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @PostMapping()
    void create(){
        //New Car
        CarCreatedEvent carCreatedEvent = new CarCreatedEvent(
            UUID.randomUUID().toString(),
            "peugeot",
            "308",
                12);

        Event event = new Event(

        );

        eventStore.publish(event);
    }
}
