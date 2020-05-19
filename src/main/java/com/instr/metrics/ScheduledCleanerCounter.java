package com.instr.metrics;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class extends SimpleCounter overriding simple increment() method. Difference between ScheduledCleanerCounter and SimpleCounter
 * is, the former creates a separate thread to clean the expired element from the counter, hence making increment() method faster
 * compared to SimpleCounter. For this reason it also implements CounterTask
 * @author Deepan Sathyamoorthy
 */
public class ScheduledCleanerCounter extends SimpleCounter implements CounterTask {

    private Timer timer;

    public ScheduledCleanerCounter(String name) {
        super(name);
        timer = new Timer();
    }

    public ScheduledCleanerCounter(String name, Duration duration) {
        super(name, duration);
        timer = new Timer();
    }

    /**
     * Based on the counter duration, removes all the expired elements from the counter
     * @param period in millis - frequency of the cleaner. The value 60 * 1000 executes the cleaner every minute
     */
    @Override
    public void startCleaner(long period) {
        TimerTask task = new CounterCleanerTask(counter, duration);
        timer.schedule(task, 0, period);
    }

    @Override
    public void stopCleaner() {
        timer.cancel();
        timer.purge();
    }

    @Override
    public boolean increment() {
        return counter.add(Instant.now());
    }
}
