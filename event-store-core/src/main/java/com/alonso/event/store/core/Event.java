package com.alonso.event.store.core;

import java.util.HashMap;
import java.util.UUID;

public class Event {
    private String id;
    private Long sequenceNumber;
    private String correlationId; //TODO
    private String stream;
    private String streamId;
    private int streamVersion;
    private String eventType;
    private Payload payload;
    private HashMap metadata; //TODO

    public Event(){
    }

    public Event(Payload payload) {
        this.id = UUID.randomUUID().toString();
        this.payload = payload;
        this.stream = payload.getAggregate().getSimpleName();
        this.streamId = payload.getAggregateId();
        this.streamVersion = payload.getAggregateVersion();
        this.eventType = payload.getClass().getSimpleName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public int getStreamVersion() {
        return streamVersion;
    }

    public void setStreamVersion(int streamVersion) {
        this.streamVersion = streamVersion;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public HashMap getMetadata() {
        return metadata;
    }

    public void setMetadata(HashMap metadata) {
        this.metadata = metadata;
    }
}
