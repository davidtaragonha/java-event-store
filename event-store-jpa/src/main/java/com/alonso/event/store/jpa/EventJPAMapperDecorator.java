package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class EventJPAMapperDecorator implements EventJPAMapper {

    @Autowired
    private PayloadSerializer payloadSerializer;

    @Autowired
    @Qualifier("delegate")
    private EventJPAMapper delegate;

    @Override
    public EventJPA to(Event event) {
        EventJPA e = delegate.to(event);
        e.setPayload(payloadSerializer.payloadTo(event.getPayload()));
        return e;
    }

    @Override
    public Event from(EventJPA eventJPA) {
        Event e = delegate.from(eventJPA);
        e.setPayload(payloadSerializer.payLoadFrom(eventJPA.getEventType(), eventJPA.getPayload()));
        return e;
    }
}
