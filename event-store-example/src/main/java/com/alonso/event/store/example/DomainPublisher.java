package com.alonso.event.store.example;

import com.alonso.event.store.core.Entity;
import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DomainPublisher {
    private final EventStore eventStore;

    @Autowired
    public DomainPublisher(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void publish(Entity entity){
        List<Event> events = entity.events()
                .stream()
                .map(Event::new)
                .collect(Collectors.toList());

        eventStore.publish(events);
    }
}
