package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Payload;

public interface PayloadSerializer {
    String payloadTo(Payload payload);
    Payload payLoadFrom(String eventType, String payload);
}
