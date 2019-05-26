package com.alonso.event.store.jpa;

import java.util.concurrent.atomic.AtomicInteger;

public class PublisherStatus {
    private static final AtomicInteger publisherCounter = new AtomicInteger(0);
    private String id;
    private int status;

    public PublisherStatus() {
        this.id = "n_"+publisherCounter.incrementAndGet();
        this.opened();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void opened() {
        this.status = 1;
    }

    public void error() {
        this.status = 3;
    }

    public void closed() {
        this.status = 2;
    }
}
