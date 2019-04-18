package com.brandwatch.metrics;

import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;

@Component
public class MetricsFacade {

    private static final Logger logger = LoggerFactory.getLogger(MetricsFacade.class);

    public static final String METRICS_PREFIX = "concurrent-metrics-publisher..";
    public static final String METRIC_NAME = "increments";

    private final MeterRegistry meterRegistry;
    private final Semaphore semaphore;

    public MetricsFacade(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.semaphore = new Semaphore(1);
        new ClassLoaderMetrics().bindTo(meterRegistry);
        new JvmMemoryMetrics().bindTo(meterRegistry);
        new JvmGcMetrics().bindTo(meterRegistry);
        new JvmThreadMetrics().bindTo(meterRegistry);
        new ProcessorMetrics().bindTo(meterRegistry);
    }

    public void incrementWithLocking() {
        try {
            semaphore.acquire();
            meterRegistry.counter(METRIC_NAME).increment();
            semaphore.release();
        } catch (InterruptedException ex) {
            logger.warn("Interrupted", ex);
            Thread.currentThread().interrupt();
        }
    }

    public void incrementWithoutLocking() {
        meterRegistry.counter(METRIC_NAME).increment();
    }
}
