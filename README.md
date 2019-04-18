# Concurrent Metrics Publisher

A MVP for investigating potential concurrency bugs in micrometer's telegraf flavoured statsd publisher.

## Overview

This is a spring-boot app that can be run with `mvn spring-boot:run`. Alterations to App.java can demonstrate
concurrent vs single-threaded behaviour. By default, the metrics will be published without common tags.
