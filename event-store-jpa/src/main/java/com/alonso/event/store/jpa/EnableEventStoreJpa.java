package com.alonso.event.store.jpa;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Import(ConfigurationEventStoreJpa.class)
public @interface EnableEventStoreJpa {
}
