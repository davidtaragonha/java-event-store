package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import io.micrometer.core.instrument.MeterRegistry;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

//TODO Use querydls to do queries with predicates
@Component
public class EventStoreJPA implements EventStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventStoreJPA.class);

    private final EventRepositoryJPA eventRepositoryJPA;
    private final EventJPAMapper eventJPAMapper;
    private final EventStoreProperties eventStoreProperties;
    private final MeterRegistry meterRegistry;
    private final EventProcessor eventProcessor;

    @Autowired
    public EventStoreJPA(EventRepositoryJPA eventRepositoryJPA,
                         EventJPAMapper eventJPAMapper,
                         EventStoreProperties eventStoreProperties, MeterRegistry meterRegistry, EventProcessor eventProcessor) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.eventJPAMapper = eventJPAMapper;
        this.eventStoreProperties = eventStoreProperties;
        this.meterRegistry = meterRegistry;
        this.eventProcessor = eventProcessor;
    }

    public void publish(Event event) {
        meterRegistry.counter("event_store_publish","event",event.getEventType()).increment();
        eventRepositoryJPA.save(eventJPAMapper.to(event));
    }

    public void publish(List<Event> events) {
        List<EventJPA> eventsJPA = events
            .stream()
            .peek(event -> meterRegistry.counter("event_store_publish","event",event.getEventType()).increment())
            .map(eventJPAMapper::to)
            .collect(Collectors.toList());

        eventRepositoryJPA.saveAll(eventsJPA);
    }

    public Publisher<Event> forSequence(long sequenceNumber) {
        return Flux.create(fluxSink ->
            retrieveEvents(
                sequenceNumber,
                fluxSink,
                eventRepositoryJPA::findBySequenceNumberGreaterThan
            )
        );
    }

    public Publisher<Event> forStream(String stream, long sequenceNumber) {
        return Flux.create(fluxSink ->
            retrieveEvents(
                sequenceNumber,
                fluxSink,
                seq -> eventRepositoryJPA.findByStreamAndSequenceNumberGreaterThan(stream, seq)
            )
        );
    }

    public Publisher<Event> forStreamId(String streamId, long sequenceNumber) {
        return Flux.create(fluxSink ->
            retrieveEvents(
                sequenceNumber,
                fluxSink,
                seq -> eventRepositoryJPA.findByStreamIdAndSequenceNumberGreaterThan(streamId, seq)
            )
        );
    }

    private void retrieveEvents(long sequenceNumber,
                                FluxSink<Event> fluxSink,
                                Function<Long, Stream<EventJPA>> resultSet) {

        //TODO Register al subscriptions with its status
        AtomicInteger gauge = meterRegistry.gauge(Thread.currentThread().getName(), new AtomicInteger(1));
        try{
            Mutable<Long> seqMutable = new Mutable<>(sequenceNumber);
            while(!fluxSink.isCancelled()){
                eventProcessor.process(seqMutable, fluxSink, resultSet);
                sleep();
            }
        }
        finally {
            gauge.decrementAndGet();
        }
        LOGGER.info("Closed the publisher");
    }

    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(eventStoreProperties.getDelay());
        } catch (InterruptedException e) {
            LOGGER.error("Sleeping the event store to query the next events");
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    public static class Mutable<T> {
        private T value;

        private Mutable(T value) {
            setValue(value);
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            requireNonNull(value, "Can not be null the value for mutable object");
            this.value = value;
        }
    }
}
