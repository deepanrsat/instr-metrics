package com.instr.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TimerTask;

import static java.util.Objects.requireNonNull;

/**
 * This is a timer task that simply remove elements from the counter that is older the CurrentTime - Duration
 * If the duration is 10 minutes, then it will simply remove all the elements that are older than currenttime - 10 minutes
 * @author Deepan Sathyamoorthy
 */
public class CounterCleanerTask extends TimerTask {

    private static final Logger LOG = LogManager.getLogger(CounterCleanerTask.class);
    private final SortedSet<Instant> counter;
    private final Duration duration;

    public CounterCleanerTask(SortedSet<Instant> counter, Duration duration) {
        this.counter = requireNonNull(counter, "Counter is null");
        this.duration = requireNonNull(duration, "Maximum duration for the counter is null");
    }

    @Override
    public void run() {
        try {
            LOG.info("Size of the counter {}", counter.size());
            removeExpiredCount(counter, duration);
        } catch (Exception e) {
            LOG.error("Error removing the expired counts", e);
        }
    }

    public static void removeExpiredCount(SortedSet<Instant> counter, Duration duration) {
        SortedSet<Instant> discard = counter.headSet(Instant.now().minus(duration));
        if(discard.size() > 0) {
            LOG.info("Removing {} elements from the counter", discard.size());
            for (Iterator<Instant> iterator = discard.iterator(); iterator.hasNext();) {
                iterator.next();
                iterator.remove();
            }
        }
    }
}
