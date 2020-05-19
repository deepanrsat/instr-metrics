package com.instr.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deepan Sathyamoorthy
 */
class CounterCleanerTaskTest {

  private CounterCleanerTask target;
  private SortedSet<Instant> counter;

  @BeforeEach
  void setUp() {
    counter = new TreeSet<>();
    target = new CounterCleanerTask(counter, Duration.ofSeconds(2));
  }

  @Test
  public void testConstructorParameters() {
    Exception exception1 = assertThrows(NullPointerException.class, () -> new CounterCleanerTask(null, null));
    assertEquals("Counter is null", exception1.getMessage());
    Exception exception2 = assertThrows(NullPointerException.class, () -> new CounterCleanerTask(counter, null));
    assertEquals("Maximum duration for the counter is null", exception2.getMessage());
  }

  @Test
  void run() {
    counter.add(Instant.now().minusSeconds(2));
    counter.add(Instant.now().minusSeconds(1));
    counter.add(Instant.now());
    target.run();
    assertEquals(2, counter.size());
  }

  @Test
  void removeExpiredCount() {
    counter.add(Instant.now().minusSeconds(2));
    counter.add(Instant.now().minusSeconds(1));
    counter.add(Instant.now());
    target.removeExpiredCount(counter, Duration.ofSeconds(1));
    assertEquals(1, counter.size());
  }
}