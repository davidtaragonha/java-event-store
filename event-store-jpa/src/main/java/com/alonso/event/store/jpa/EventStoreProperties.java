package com.alonso.event.store.jpa;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "event-store")
public class EventStoreProperties {

    private int delay;

    public EventStoreProperties() {
        //Default values
        this.delay = 1;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
