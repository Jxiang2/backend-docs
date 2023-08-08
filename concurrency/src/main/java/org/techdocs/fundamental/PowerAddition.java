package org.techdocs.fundamental;

import java.math.BigInteger;

public class PowerAddition {

  public static void main(final String[] args) throws InterruptedException {
    final PowerAddition powerAddition = new PowerAddition();
    System.out.println(
      powerAddition.calculateResult(
        BigInteger.valueOf(50),
        BigInteger.valueOf(100),
        BigInteger.valueOf(50),
        BigInteger.valueOf(100)
      )
    );
  }

  public BigInteger calculateResult(final BigInteger base1,
                                    final BigInteger power1,
                                    final BigInteger base2,
                                    final BigInteger power2) throws InterruptedException {
    final BigInteger result;
    final PowerAdditionThread thread1 = new PowerAdditionThread(base1, power1);
    final PowerAdditionThread thread2 = new PowerAdditionThread(base2, power2);

    thread1.start();
    thread2.start();


    thread1.join(50);
    thread2.join(50);


    result = thread1.getResult().add(thread2.getResult());


    return result;
  }

  private static class PowerAdditionThread extends Thread {

    private final BigInteger base;

    private final BigInteger power;

    private BigInteger result = BigInteger.ONE;

    PowerAdditionThread(final BigInteger base, final BigInteger power) {
      this.base = base;
      this.power = power;
    }

    @Override
    public void run() {
      for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
        result = result.multiply(base);
      }
    }

    public BigInteger getResult() {
      return result;
    }

  }

}