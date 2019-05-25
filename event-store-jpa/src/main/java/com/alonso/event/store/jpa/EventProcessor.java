package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.FluxSink;

import java.util.stream.StreamSupport;

@Component
@Transactional(readOnly = true)
public class EventProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);
    private final EventJPAMapper eventJPAMapper;
    private final EventRepositoryJPA eventRepositoryJPA;

    @Autowired
    public EventProcessor(EventJPAMapper eventJPAMapper, EventRepositoryJPA eventRepositoryJPA) {
        this.eventJPAMapper = eventJPAMapper;
        this.eventRepositoryJPA = eventRepositoryJPA;
    }

    public void process(EventStoreJPA.Mutable<Long> seqMutable,
                        FluxSink<Event> fluxSink,
                        Predicate predicate) {
        StreamSupport.stream(
                eventRepositoryJPA.findAll(
                        QEventJPA.eventJPA.sequenceNumber.gt(seqMutable.getValue())
                                .and(predicate))
                                .spliterator(),
                false
        )
        .map(this::parseEvent)
        .forEach(event -> {
            fluxSink.next(event);
            seqMutable.setValue(event.getSequenceNumber());
        });
    }

    private Event parseEvent(EventJPA eventJPA) {
        try {
            return eventJPAMapper.from(eventJPA);
        }
        catch (Exception ex){
            LOGGER.error("Failed mapping eventJPA to Event", ex);
            throw ex;
        }
    }
}
