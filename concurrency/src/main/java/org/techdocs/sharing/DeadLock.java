package org.techdocs.sharing;

import java.util.Random;

/**
 * Locking Strategies & Deadlocks
 */
public class DeadLock {

  public static void main(final String[] args) {
    final Intersection intersection = new Intersection();
    final Thread trainAThread = new Thread(new TrainA(intersection));
    final Thread trainBThread = new Thread(new TrainB(intersection));

    trainAThread.start();
    trainBThread.start();
  }

  public static class TrainB implements Runnable {

    private final Intersection intersection;

    private final Random random = new Random();

    public TrainB(final Intersection intersection) {
      this.intersection = intersection;
    }

    @Override
    public void run() {
      while (true) {
        final long sleepingTime = random.nextInt(5);
        try {
          Thread.sleep(sleepingTime);
        } catch (final InterruptedException e) {
        }

        intersection.takeRoadB();
      }
    }

  }

  public static class TrainA implements Runnable {

    private final Intersection intersection;

    private final Random random = new Random();

    public TrainA(final Intersection intersection) {
      this.intersection = intersection;
    }

    @Override
    public void run() {
      while (true) {
        final long sleepingTime = random.nextInt(5);
        try {
          Thread.sleep(sleepingTime);
        } catch (final InterruptedException e) {
        }

        intersection.takeRoadA();
      }
    }

  }

  public static class Intersection {

    private final Object roadA = new Object();

    private final Object roadB = new Object();

    public void takeRoadA() {
      synchronized (roadA) {
        System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

        synchronized (roadB) {
          System.out.println("Train is passing through road A");

          // Passing through road A ...
          try {
            Thread.sleep(1);
          } catch (final InterruptedException e) {
          }
        }
      }
    }

    public void takeRoadB() {
      synchronized (roadB) {
        System.out.println("Road B is locked by thread " + Thread.currentThread().getName());

        synchronized (roadA) {
          System.out.println("Train is passing through road B");

          // Passing through road B ...
          try {
            Thread.sleep(1);
          } catch (final InterruptedException e) {
          }
        }
      }
    }

  }

}
