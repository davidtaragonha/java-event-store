package com.alonso.event.store.jpa;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "IDX_SEQ_STREAM", columnList = "sequenceNumber,stream"),
        @Index(name = "IDX_SEQ_STREAM_ID", columnList = "sequenceNumber,streamId")
})
public class EventJPA {

    @Id
    private String id;
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long sequenceNumber;
    private String correlationId;
    private String stream;
    private String streamId;
    private int streamVersion;
    private String eventType;
    private String payload;
    private String metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
