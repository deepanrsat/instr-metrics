package com.instr.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.instr.metrics.CounterCleanerTask.removeExpiredCount;
import static java.util.Objects.requireNonNull;

/**
 * @author Deepan Sathyamoorthy
 */
public class SimpleCounter implements Counter {

    private static final Logger LOG = LogManager.getLogger(SimpleCounter.class);
    private final String name;
    protected final Duration duration;
    protected final SortedSet<Instant> counter;

    public SimpleCounter(String name) {
        this.name = requireNonNull(name, "Name of the metric is null");;
        this.duration = Duration.ofMinutes(10);
        this.counter = Collections.synchronizedSortedSet(new TreeSet<>());
    }

    public SimpleCounter(String name, Duration duration) {
        this.name = requireNonNull(name, "Name of the metric is null");
        this.duration = requireNonNull(duration, "Maximum duration for the counter is null");
        this.counter = Collections.synchronizedSortedSet(new TreeSet<>());
    }

    /**
     * Add a new element to the counter, by recording the Instant metric is registered. Once added, it also removes the expired
     * elements from the counter
     * @return status of addition to the counter
     */
    @Override
    public boolean increment() {
        boolean result = counter.add(Instant.now());
        try {
            removeExpiredCount(counter, duration);
        } catch (Exception e) {
            LOG.error("Error removing expired values from counter", e);
        }
        return result;
    }

    /**
     * Method to get the number of element in the counter from giveTime to currentTime. If the total duration of
     * (currentTime - fromTime) is more than configured duration, then it simply returns the counter value for maximum duration
     * @param instant fromTime in UTC
     * @return metric counter value
     */
    @Override
    public int getCount(Instant instant) {
        Instant max = Instant.now().minus(duration);
        if(instant != null && max.isBefore(instant)) {
            return counter.tailSet(instant).size();
        } else {
            LOG.info("Received time duration is beyond the max duration");
            return counter.tailSet(max).size();
        }
    }

    /**
     * Clear the counter
     */
    @Override
    public void clear() {
        LOG.info("Clearing {} counter", name);
        counter.clear();
    }

    public String getName() {
        return name;
    }

    /**
     * This method is mainly used for testing.
     * @return size of the counter
     */
    protected int getCounterSize() {
        return counter.size();
    }
}
