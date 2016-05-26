package org.chesscorp.club.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging implementation of performance monitoring.
 */
@Component
public class PerformanceMonitorImpl implements PerformanceMonitor {
    private Logger logger = LoggerFactory.getLogger(PerformanceMonitorImpl.class);
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public void mark() {
        startTime.set(System.currentTimeMillis());
    }

    @Override
    public void register(String component, String operation, long items, String itemType) {
        long now = System.currentTimeMillis() + 1;
        long duration = now - startTime.get();
        register(component, operation, items, itemType, duration);
    }

    @Override
    public void register(String component, String operation, long items, String itemType, long duration) {
        String message = String.format("%20s - %-16s : %5d %s(s) in %8.3fs (%6.1f %s(s)/s)",
                component, operation, items, itemType, duration / 1000., 1000. * items / duration, itemType);

        logger.debug(message);
    }
}
