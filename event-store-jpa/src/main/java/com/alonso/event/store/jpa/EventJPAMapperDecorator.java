package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class EventJPAMapperDecorator implements EventJPAMapper {

    @Autowired
    private PayloadSerializer payloadSerializer;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    @Qualifier("delegate")
    private EventJPAMapper delegate;

    @Override
    public EventJPA to(Event event) {
        return meterRegistry.timer("event_store_mapper_to_event_jpa","event", event.getEventType())
            .record(()-> {
                EventJPA e = delegate.to(event);
                e.setPayload(payloadSerializer.payloadTo(event.getPayload()));
                return e;
        });
    }

    @Override
    public Event from(EventJPA eventJPA) {
        return meterRegistry.timer("event_store_mapper_to_event","event", eventJPA.getEventType())
            .record(() -> {
                Event e = delegate.from(eventJPA);
                e.setPayload(payloadSerializer.payLoadFrom(eventJPA.getEventType(), eventJPA.getPayload()));
                return e;
        });
    }
}
