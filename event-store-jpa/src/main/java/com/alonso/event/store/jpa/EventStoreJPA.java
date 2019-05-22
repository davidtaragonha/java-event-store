package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
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

    @Autowired
    public EventStoreJPA(EventRepositoryJPA eventRepositoryJPA, EventJPAMapper eventJPAMapper) {
        this.eventRepositoryJPA = eventRepositoryJPA;
        this.eventJPAMapper = eventJPAMapper;
    }

    public void publish(Event event) {
        eventRepositoryJPA.save(eventJPAMapper.to(event));
    }

    public void publish(List<Event> events) {
        List<EventJPA> eventsJPA = events
            .stream()
            .map(eventJPAMapper::to)
            .collect(Collectors.toList());

        eventRepositoryJPA.saveAll(eventsJPA);
    }

    public Publisher<Event> forSequence(long sequenceNumber) {
        return Flux.create(fluxSink ->
            retrieveEvents(
                sequenceNumber,
                fluxSink,
                eventRepositoryJPA::findBySequenceNumber
            )
        );
    }

    public Publisher<Event> forStream(String stream, long sequenceNumber) {
        return Flux.create(fluxSink ->
            retrieveEvents(
                sequenceNumber,
                fluxSink,
                seq -> eventRepositoryJPA.findByStreamAndSequenceNumber(stream, seq)
            )
        );
    }

    public Publisher<Event> forStreamId(String streamId, long sequenceNumber) {
        return Flux.create(fluxSink ->
            retrieveEvents(
                sequenceNumber,
                fluxSink,
                seq -> eventRepositoryJPA.findByStreamIdAndSequenceNumber(streamId, seq)
            )
        );
    }

    private void retrieveEvents(long sequenceNumber,
                                FluxSink<Event> fluxSink,
                                Function<Long,Stream<EventJPA>> resultSet) {
        Mutable<Long> seqMutable = new Mutable<>(sequenceNumber);
        while(true){
            resultSet.apply(seqMutable.getValue())
                .map(eventJPA -> {
                    try {
                        return eventJPAMapper.from(eventJPA);
                    }
                    catch (Exception ex){
                        //TODO Create lambda to wrap exceptions
                        LOGGER.error("Failed mapping eventJPA to Event", ex);
                        throw ex;
                    }
                })
                .forEach(event -> {
                    fluxSink.next(event);
                    seqMutable.setValue(event.getSequenceNumber());
                });
        }
    }

    private static class Mutable<T> {
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
