package com.instr.metrics;

import java.time.Instant;

/**
 * @author Deepan Sathyamoorthy
 */
public interface Counter {

    /**
     * Method to increase the counter by 1
     * @return true if the operation was successful, else false
     */
    boolean increment();

    /**
     * Method to get the counter value from given time to current time.
     * @param fromTime in UTC. Instant.now() by default give time in UTC
     * @return number of elements in the counter from given fromTime to current time.
     */
    int getCount(Instant fromTime);

    /**
     * Method to clear all the counter
     * @return true if the operation is successful, else false.
     */
    void clear();
}
