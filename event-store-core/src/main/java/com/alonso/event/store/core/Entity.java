package com.alonso.event.store.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;

public abstract class Entity {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HashMap<String, Method> cacheApplyMethods = new HashMap<>();
    private final List<Payload> events = new ArrayList<>();

    protected long version;

    public long getVersion() {
        return version;
    }

    protected void publish(Payload payload){
        applyPayload(payload);

        this.version++;
        events.add(payload);
    }

    private void applyPayload(Payload payload){
        try{
            Method method = cacheApplyMethods.get(payload.getClass().getSimpleName());
            if(isNull(method)){
                method = this.getClass().getDeclaredMethod("apply", payload.getClass());
                cacheApplyMethods.put(payload.getClass().getSimpleName(), method);
            }
            method.invoke(this, payload);
        }
        catch (Exception ex){
            LOGGER.error("Applying event {} to the entity {}", payload.getClass().getSimpleName(), this.getClass().getSimpleName());
            throw new UnsupportedOperationException(ex);
        }
    }

    public List<Payload> events(){
        return Collections.unmodifiableList(events);
    }

    protected long nextVersion(){
        return ++this.version;
    }
}
