# instr-metrics

Simple time based counter to capture metric values in jvm environment.

## Requirements 

* Mac OS X or Linux
* Java 11.0.6, 64-bit. Both Oracle JDK and OpenJDK are supported.
* Maven 3.6.3+ (for building)

## Building instr-metrics

Instr-metrics is a standard Maven project. Simply run the following command from the project root directory, after setting up your maven and java

    mvn clean install
    
## Usage

It supports two types of counter 

* Simple Counter
* Scheduled Cleaner Counter

#### Simple Counter

This counter is a simple time based counter, that record the metric by simply adding timestamp to record the increment. 
It is ideal if you want to record the metrics like ERRORS, HTTP_SERVED

    SimpleCounter counter = new SimpleCounter("HTTP_SERVED");

The above will simple create a counter with default max duration of 10 minutes. Anytime older than past 10 minutes are automatically removed when incrementing counter.
For every increment, it make sure there is not expired element in the counter

    SimpleCounter httpServedCounter = new SimpleCounter("HTTP_SERVED", Duration.ofMinutes(10));

Counter with customizable duration

To retrieve the counter value simply provide the fromTime in UTC. The below code, returns counter metric for last 10 seconds
    
    httpServedCounter.getCount(Instant.now().minusSeconds(10));

#### ScheduledCleanerCounter

This counter is also time based counter, similar to SimpleCounter, it also records the metric, by adding timestamp to the counter.
However it creates separate timer task to clean the expired elements from the counter. This is faster compared to SimpleCounter, since it removes the overhead of cleaning the counter to separate thread.

    ScheduledCleanerCounter counter = new ScheduledCleanerCounter("HTTP_SERVED", Duration.ofMinutes(10));