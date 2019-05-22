package com.alonso.event.store.core;

import org.reactivestreams.Publisher;

import java.util.List;

public interface EventStore {
    public void publish(Event event);
    public void publish(List<Event> events);
    public Publisher<Event> forSequence(long sequenceNumber);
    public Publisher<Event> forStream(String stream, long sequenceNumber);
    public Publisher<Event> forStreamId(String streamId, long sequenceNumber);
}
