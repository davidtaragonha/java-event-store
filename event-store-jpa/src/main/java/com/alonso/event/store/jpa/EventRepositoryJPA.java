package com.alonso.event.store.jpa;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface EventRepositoryJPA extends CrudRepository<EventJPA, String>, QuerydslPredicateExecutor<EventJPA> {
    Stream<EventJPA> findBySequenceNumberGreaterThan(long sequenceNumber);
    Stream<EventJPA> findByStreamAndSequenceNumberGreaterThan(String stream, long sequenceNumber);
    Stream<EventJPA> findByStreamIdAndSequenceNumberGreaterThan(String streamId, long sequenceNumber);
}
