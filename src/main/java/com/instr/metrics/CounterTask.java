package com.instr.metrics;

/**
 * @author Deepan Sathyamoorthy
 */
public interface CounterTask {
    /**
     * Method to start the cleaner task to remove expired elements from the counter.
     * @param period in millis - frequency of the cleaner. The value 60 * 1000 executes the cleaner every minute
     */
    void startCleaner(long period);

    /**
     * Method to simply stop cleaner from removing the expired elements from the counter.
     */
    void stopCleaner();
}
