package org.chesscorp.club.monitoring;

import org.junit.Test;

public class PerformanceMonitorTest {

    @Test
    public void testPerformanceRegistration() {
        PerformanceMonitor monitor = new PerformanceMonitorImpl();
        monitor.register("Component 1", "operation", 12, "item", 1200);
    }

    @Test
    public void testMarkAndRegister() throws InterruptedException {
        PerformanceMonitor monitor = new PerformanceMonitorImpl();
        monitor.mark();
        Thread.sleep(5);
        monitor.register("Component 1", "operation", 12, "item");
    }

}
