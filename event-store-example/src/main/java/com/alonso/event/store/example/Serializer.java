package com.alonso.event.store.example;

import com.alonso.event.store.core.Payload;
import com.alonso.event.store.jpa.PayloadSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class Serializer implements PayloadSerializer {

    private final static Logger LOGGER = LoggerFactory.getLogger(Serializer.class);
    private final ObjectMapper mapper;

    @Autowired
    public Serializer(Optional<List<StdSerializer>> stdSerializers, Optional<List<StdDeserializer>> stdDeserializers) {
        SimpleModule module = new SimpleModule();
        stdSerializers.orElse(Collections.emptyList())
            .forEach(stdSerializer ->
                module.addSerializer(getPayLoadClass(stdSerializer.getClass()), stdSerializer)
            );

        stdDeserializers.orElse(Collections.emptyList())
            .forEach(stdDeserializer ->
                module.addDeserializer(getPayLoadClass(stdDeserializer.getClass()), stdDeserializer)
            );

        mapper = new ObjectMapper().registerModule(module);
    }

    @Override
    public String payloadTo(Payload payload) {
        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            LOGGER.error("Could not serialize the payload {}", payload.getClass().getSimpleName());
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Payload payLoadFrom(String eventType, String payload) {
        try {
            //TODO calculate full name
            return (Payload) mapper.readValue(payload, Class.forName("com.alonso.event.store.example.domain.event."+eventType));
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Could not deserialize the eventType {} with this payload {]", eventType, payload);
            throw new IllegalStateException(e);
        }
    }

    private Class getPayLoadClass(Class std){
        return (Class) ((ParameterizedType) std.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
