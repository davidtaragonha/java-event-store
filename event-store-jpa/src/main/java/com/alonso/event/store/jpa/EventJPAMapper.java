package com.alonso.event.store.jpa;

import com.alonso.event.store.core.Event;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashMap;

@Mapper
@DecoratedWith(EventJPAMapperDecorator.class)
public interface EventJPAMapper {

    @Mapping(target = "payload", ignore = true)
    EventJPA to(Event event);

    @Mapping(target = "payload", ignore = true)
    Event from(EventJPA eventJPA);

    default String metaDataTo(HashMap metaData){
        return "";
    }

    default HashMap metaDataFrom(String metaData){
        return null;
    }
}
