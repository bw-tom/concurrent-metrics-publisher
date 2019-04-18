package com.brandwatch.metrics.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.NamingConvention;

@Configuration
public class MetricsConfig {

    private static final Logger logger = LoggerFactory.getLogger(MetricsConfig.class);

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCommonTags(
            @Value("${concurrent-metrics-publisher.host}") String host,
            @Value("${concurrent-metrics-publisher.instance.id}") String instanceId
    ) {
        logger.info("Hostname: {}", host);
        logger.info("Instance ID: {}", instanceId);
        return registry -> registry.config().commonTags(Tags.of("host", host,
                                                                "instance", instanceId));
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryNamingConvention() {
        return registry -> registry.config().namingConvention(NamingConvention.dot);
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryPrefixFilter() {
        return registry -> registry.config().meterFilter(new MeterFilter() {
            @Override
            public Id map(Id id) {
                if (id.getName().startsWith("concurrent-metrics-publisher.")) {
                    return id;
                }
                return id.withName("concurrent-metrics-publisher." + id.getName());
            }
        });
    }

}
