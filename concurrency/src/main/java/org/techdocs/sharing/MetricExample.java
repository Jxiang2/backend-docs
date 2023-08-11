package org.techdocs.sharing;

import java.util.Random;

/**
 * Atomic Operations, Volatile & Metrics practical example
 */
public class MetricExample {

  public static void main(final String[] args) {
    final MinMaxMetric metrics = new MinMaxMetric();

    final BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);
    final BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);

    final MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

    businessLogicThread1.start();
    businessLogicThread2.start();
    metricsPrinter.start();
  }

  public static class MetricsPrinter extends Thread {

    private final MinMaxMetric metrics;

    public MetricsPrinter(final MinMaxMetric metrics) {
      this.metrics = metrics;
    }

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(10);
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }

        final double currentMax = metrics.getMax();
        final double currentMin = metrics.getMin();

        System.out.println("Current max is " + currentMin);
        System.out.println("Current min is " + currentMax);
      }
    }

  }

  public static class BusinessLogic extends Thread {

    private final MinMaxMetric metrics;

    private final Random random = new Random();

    public BusinessLogic(final MinMaxMetric metrics) {
      this.metrics = metrics;
    }

    @Override
    public void run() {
      while (true) {
        final long start = System.currentTimeMillis();

        // Business logic
        try {
          Thread.sleep(random.nextInt(10));
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }

        final long end = System.currentTimeMillis();

        metrics.addSample(end - start);
      }
    }

  }


  // Example 2
  public static class MinMaxMetric {

    private volatile long minValue;

    private volatile long maxValue;

    /**
     * Initializes all member variables
     */
    public MinMaxMetric() {
      this.maxValue = Long.MIN_VALUE;
      this.minValue = Long.MAX_VALUE;
    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(final long newSample) {
      synchronized (this) {
        this.minValue = Math.min(newSample, this.minValue);
        this.maxValue = Math.max(newSample, this.maxValue);
      }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
      return this.minValue;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
      return this.maxValue;
    }

  }

  // Example 1
  public static class AvgMetric {

    private long count = 0;

    // Double is not atomic, so we need to use volatile to ensure that the value is always read atomically
    private volatile double average = 0.0;

    public synchronized void addSample(final long sample) {
      final double currentSum = average * count;
      count++;
      average = (currentSum + sample) / count; // This is atomic because volatile keyword added to average
    }

    // This is atomic because volatile keyword added to average
    public double getAverage() {
      return average;
    }

  }

}
