package com.alonso.event.store.example.domain.model;

import com.alonso.event.store.core.Payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entity {
    private final List<Payload> events = new ArrayList<>();

    protected void publish(Payload payload){
        events.add(payload);
    }

    public List<Payload> events(){
        return Collections.unmodifiableList(events);
    }
}
