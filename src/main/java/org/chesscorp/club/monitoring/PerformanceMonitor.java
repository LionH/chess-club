package org.chesscorp.club.monitoring;

/**
 * Performance metrics registration interface.
 */
public interface PerformanceMonitor {

    /**
     * Mark the start of an operation. Must be called before registering the operation if the duration is not
     * supplied by the caller.
     */
    void mark();

    /**
     * Complete registration of an activity. The duration is calculated since the last call to the mark method.
     *
     * @param component name of the activity component
     * @param operation name of the operation
     * @param items     number of processed items
     * @param itemType  type of item
     */
    void register(String component, String operation, long items, String itemType);

    /**
     * Complete registration of an activity.
     *
     * @param component name of the activity component
     * @param operation name of the operation
     * @param items     number of processed items
     * @param itemType  type of item
     * @param duration  duration of the operation in milliseconds
     */
    void register(String component, String operation, long items, String itemType, long duration);
}
