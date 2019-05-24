package com.alonso.event.store.example.domain.event;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CarCreatedDeserializer extends StdDeserializer<CarCreatedEvent> {

    public CarCreatedDeserializer() {
        super(CarCreatedEvent.class);
    }

    @Override
    public CarCreatedEvent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return new CarCreatedEvent(
                node.get("id").asText(),
                node.get("company").asText(),
                node.get("model").asText(),
                node.get("prize").asDouble());
    }
}
