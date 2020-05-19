package com.instr.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test has Thread.sleep() to imitate the duration and test the counter values
 * @author Deepan Sathyamoorthy
 */
class ScheduledCleanerCounterTest {

  private ScheduledCleanerCounter target;

  @BeforeEach
  public void setUp() {
    target = new ScheduledCleanerCounter("HTTP_SERVED");
  }

  @Test
  public void testConstructorParameters() {
    Exception exception1 = assertThrows(NullPointerException.class, () -> new ScheduledCleanerCounter(null));
    assertEquals("Name of the metric is null", exception1.getMessage());
    Exception exception2 = assertThrows(NullPointerException.class, () -> new ScheduledCleanerCounter("ERROR", null));
    assertEquals("Maximum duration for the counter is null", exception2.getMessage());
  }

  @Test
  public void increment() {
    assertTrue(target.increment());
    int val = target.getCount(Instant.now().minusSeconds(30));
    assertEquals(1, val);
  }

  @Test
  public void testGetCount() {
    assertTrue(target.increment());
    assertEquals(1, target.getCount(Instant.now().minusSeconds(3)));
    assertEquals("HTTP_SERVED", target.getName());
  }

  @Test
  public void incrementScheduledCleaner() throws InterruptedException {
    target = new ScheduledCleanerCounter("ERRORS", Duration.ofSeconds(10));
    target.startCleaner(1000);
    for(int i=0; i<10; i++) {
      target.increment();
    }
    Thread.sleep(11 * 1000);
    target.increment();
    assertEquals(1, target.getCounterSize());
    target.stopCleaner();
  }
}