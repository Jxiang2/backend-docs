package org.techdocs.fundamental;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Joining Threads
 */
public class ThreadJoin {

  public static void main(final String[] args) throws InterruptedException {
    final List<Long> inputNumbers = Arrays.asList(10000000000L, 3435L, 35435L, 2324L, 4656L, 23L, 5556L);

    final List<FactorialThread> workerThreads = new ArrayList<>();

    for (final long inputNumber : inputNumbers) {
      workerThreads.add(new FactorialThread(inputNumber));
    }

    for (final Thread thread : workerThreads) {
      thread.setDaemon(true);
      thread.start();
    }

    for (final Thread worker : workerThreads) {
      worker.join(2000); // if thread does not finish in 2 seconds, continue
    }

    for (int i = 0; i < inputNumbers.size(); i++) {
      final FactorialThread factorialThread = workerThreads.get(i);
      if (factorialThread.isFinished()) {
        System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
      } else {
        System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
      }
    }
  }

  public static class FactorialThread extends Thread {

    private final long inputNumber;

    private BigInteger result = BigInteger.ZERO;

    private boolean isFinished = false;

    public FactorialThread(final long inputNumber) {
      this.inputNumber = inputNumber;
    }

    @Override
    public void run() {
      this.result = factorial(inputNumber);
      this.isFinished = true;
    }

    public BigInteger factorial(final long n) {
      BigInteger tempResult = BigInteger.ONE;

      for (long i = n; i > 0; i--) {
        tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
      }
      return tempResult;
    }

    public BigInteger getResult() {
      return result;
    }

    public boolean isFinished() {
      return isFinished;
    }

  }

}
