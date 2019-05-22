package com.alonso.event.store.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface EventRepositoryJPA extends CrudRepository<EventJPA, String> {
    Stream<EventJPA> findBySequenceNumber(long sequenceNumber);
    Stream<EventJPA> findByStreamAndSequenceNumber(String stream, long sequenceNumber);
    Stream<EventJPA> findByStreamIdAndSequenceNumber(String streamId, long sequenceNumber);
}
