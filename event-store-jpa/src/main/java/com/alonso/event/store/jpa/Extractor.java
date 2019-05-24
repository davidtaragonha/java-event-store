package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.FluxSink;

import java.util.function.Function;
import java.util.stream.Stream;

@Component
@Transactional(readOnly = true)
public class Extractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);
    private final EventJPAMapper eventJPAMapper;

    @Autowired
    public Extractor(EventJPAMapper eventJPAMapper) {
        this.eventJPAMapper = eventJPAMapper;
    }

    public void process(EventStoreJPA.Mutable<Long> seqMutable,
                        FluxSink<Event> fluxSink,
                        Function<Long,Stream<EventJPA>> resultSet) {

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
