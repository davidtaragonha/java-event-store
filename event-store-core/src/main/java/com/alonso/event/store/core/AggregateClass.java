package com.alonso.event.store.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

@Documented
@Inherited
public @interface AggregateClass {
    Class value();
}
