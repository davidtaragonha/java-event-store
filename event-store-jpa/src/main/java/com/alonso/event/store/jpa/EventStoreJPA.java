package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import com.alonso.event.store.core.EventStore;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return createEventPublisher(
            sequenceNumber,
            eventRepositoryJPA::findBySequenceNumberGreaterThan
        );
    }

    public Publisher<Event> forStream(String stream, long sequenceNumber) {
        return createEventPublisher(
            sequenceNumber,
            seq -> eventRepositoryJPA.findByStreamAndSequenceNumberGreaterThan(stream, seq)
        );
    }

    public Publisher<Event> forStreamId(String streamId, long sequenceNumber) {
        return createEventPublisher(
            sequenceNumber,
            seq -> eventRepositoryJPA.findByStreamIdAndSequenceNumberGreaterThan(streamId, seq)
        );
    }

    private Flux<Event> createEventPublisher(long sequenceNumber, Function<Long, Stream<EventJPA>> eventStreamSupplier) {
        return Flux.create(fluxSink -> {
            //TODO Review a way to monitoring all publishers
            PublisherStatus publisherStatus = new PublisherStatus();
            meterRegistry.gauge("event_store_publishers", Tags.of("number",publisherStatus.getId()) ,publisherStatus.getStatus());

            try{
                Mutable<Long> seqMutable = new Mutable<>(sequenceNumber);
                while(!fluxSink.isCancelled()){
                    eventProcessor.process(seqMutable, fluxSink, eventStreamSupplier);
                    sleep();
                }
            }
            catch (Exception ex){
                publisherStatus.error();
                throw ex;
            }

            publisherStatus.closed();
            LOGGER.info("Publisher closed");
        });
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
}
