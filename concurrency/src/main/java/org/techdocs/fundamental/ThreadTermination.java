package org.techdocs.fundamental;

import java.math.BigInteger;

/**
 * Thread Termination & Daemon Threads
 * Daemons are suitable for background tasks such as garbage collection, which is run by the JVM.
 */
public class ThreadTermination {

  public static void main(final String[] args) throws InterruptedException {
    final Thread thread = new Thread(
      new LongComputationTask(
        new BigInteger("2000000000"),
        new BigInteger("100000000")
      )
    );

    // Daemon thread will continue to run in the background, it will not prevent exiting the main thread
    thread.setDaemon(true);
    thread.setName("LongComputationThread");
    thread.start();

    Thread.sleep(1000);

    thread.interrupt();
  }

  private static class LongComputationTask implements Runnable {

    private final BigInteger base;

    private final BigInteger power;

    LongComputationTask(final BigInteger base, final BigInteger power) {
      this.base = base;
      this.power = power;
    }

    @Override
    public void run() {
      System.out.println(base + "^" + power + " = " + pow(base, power));
    }

    private BigInteger pow(final BigInteger base, final BigInteger power) {
      BigInteger result = BigInteger.ONE;

      for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println("Prematurely interrupted computation");
          return BigInteger.ZERO;
        }
        result = result.multiply(base);
      }

      return result;
    }

  }

}
