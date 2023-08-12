package org.techdocs.lockapi.cryptoboard.backend;

import java.util.Random;

public class PriceUpdater extends Thread {

  private final PricesContainer pricesContainer;

  private final Random random = new Random();

  public PriceUpdater(final PricesContainer pricesContainer) {
    this.pricesContainer = pricesContainer;
  }

  @Override
  public void run() {
    while (true) {
      pricesContainer.getLockObject().lock();

      // Mock API call
      try {
        // Connecting to API...
        try {
          Thread.sleep(1000);
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }

        // Get response, update prices...
        pricesContainer.setBitcoinPrice(random.nextInt(20000));
        pricesContainer.setEtherPrice(random.nextInt(2000));
        pricesContainer.setLitecoinPrice(random.nextInt(500));
        pricesContainer.setBitcoinCashPrice(random.nextInt(5000));
        pricesContainer.setRipplePrice(random.nextDouble());
      } finally {
        pricesContainer.getLockObject().unlock();
      }

      // Expensive operation...
      try {
        Thread.sleep(2000);
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
