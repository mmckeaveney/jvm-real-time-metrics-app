package com.jvm.realtime.client;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Used to annotate a custom metric for a user.
@Target(value = {ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Component
public @interface JvmRealTimeCustomMetric {
}
