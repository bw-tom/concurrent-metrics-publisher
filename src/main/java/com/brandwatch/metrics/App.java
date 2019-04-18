package com.brandwatch.metrics;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class App {

    @Autowired
    ExecutorService executorService;
    @Autowired
    MetricsFacade metricsFacade;

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    /**
     * Replace the call to metricsFacade.incrementWithoutLocking() to
     * metricsFacade.incrementWithLocking() to see the common tags correctly applied
     */
    @Autowired
    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        while (true)
            executorService.submit(() -> metricsFacade.incrementWithoutLocking());
    }
}
