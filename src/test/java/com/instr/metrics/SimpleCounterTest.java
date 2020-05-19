package com.instr.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Deepan Sathyamoorthy
 */
public class SimpleCounterTest {

    private SimpleCounter target;

    @BeforeEach
    public void setUp() {
        target = new SimpleCounter("HTTP_SERVED");
    }

    @Test
    public void testConstructorParameters() {
        Exception exception1 = assertThrows(NullPointerException.class, () -> new SimpleCounter(null));
        assertEquals("Name of the metric is null", exception1.getMessage());
        Exception exception2 = assertThrows(NullPointerException.class, () -> new SimpleCounter("ERROR", null));
        assertEquals("Maximum duration for the counter is null", exception2.getMessage());
    }

    @Test
    public void increment() {
        assertEquals("HTTP_SERVED", target.getName());
        assertTrue(target.increment());
    }

    @Test
    public void getCount() throws InterruptedException {
        target = new SimpleCounter("ERRORS", Duration.ofSeconds(8));
        for (int i=0; i<10; i++) {
            assertTrue(target.increment());
        }
        int value = target.getCount(Instant.now().minusSeconds(4));
        assertEquals(10, value);
        Thread.sleep( 9 * 1000);
        assertTrue(target.increment());
        assertTrue(target.increment());
        assertEquals(2, target.getCount(Instant.now().minusSeconds(300)));
    }

    @Test
    public void getCountEmptyCounter() {
        int val = target.getCount(Instant.now().minusSeconds(300));
        assertEquals(0, val);
    }

    @Test
    public void getCountMoreThanDuration() throws InterruptedException {
        target = new SimpleCounter("PAGE_HIT", Duration.ofSeconds(4));
        for (int i=0; i<80; i++) {
            assertTrue(target.increment());
        }
        Thread.sleep(4000);
        for (int i=0; i<20; i++) {
            assertTrue(target.increment());
        }
        int value =  target.getCount(Instant.now().minusSeconds(20));
        assertEquals(20, value);
    }

    @Test
    public void getCountNonEmptyCounterAfterMaz() throws InterruptedException {
        target = new SimpleCounter("PAGE_HIT", Duration.ofSeconds(4));
        for (int i=0; i<80; i++) {
            assertTrue(target.increment());
        }
        Thread.sleep(5000);
        int value =  target.getCount(Instant.now().minusSeconds(20));
        assertEquals(0, value);
        assertEquals(80, target.getCounterSize());
    }

    @Test
    public void getCountNullTime() {
        assertTrue(target.increment());
        int val = target.getCount(null);
        assertEquals(1, val);
    }

    @Test
    public void clear() {
        assertTrue(target.increment());
        target.clear();
        assertEquals(0, target.getCount(Instant.now().minusSeconds(30)));
    }

}