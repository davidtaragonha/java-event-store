package com.alonso.event.store.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface EventRepositoryJPA extends CrudRepository<EventJPA, String> {
    Stream<EventJPA> findByStreamId(String streamId);

    Stream<EventJPA> findBySequenceNumberGreaterThan(long sequenceNumber);
    Stream<EventJPA> findByStreamAndSequenceNumberGreaterThan(String stream, long sequenceNumber);
    Stream<EventJPA> findByStreamIdAndSequenceNumberGreaterThan(String streamId, long sequenceNumber);
}
