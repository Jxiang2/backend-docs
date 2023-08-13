package org.techdocs.datastructures;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomic Integers & Lock Free E-Commerce
 */
public class AtomicIntegers {

  public static void main(final String[] args) throws InterruptedException {
    final InventoryCounter inventoryCounter = new InventoryCounter();
    final IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
    final DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

    incrementingThread.start();
    decrementingThread.start();

    incrementingThread.join();
    decrementingThread.join();

    System.out.println("We currently have " + inventoryCounter.getItems() + " items");
  }

  public static class DecrementingThread extends Thread {

    private final InventoryCounter inventoryCounter;

    public DecrementingThread(final InventoryCounter inventoryCounter) {
      this.inventoryCounter = inventoryCounter;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10000; i++) {
        inventoryCounter.decrement();
      }
    }

  }

  public static class IncrementingThread extends Thread {

    private final InventoryCounter inventoryCounter;

    public IncrementingThread(final InventoryCounter inventoryCounter) {
      this.inventoryCounter = inventoryCounter;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10000; i++) {
        inventoryCounter.increment();
      }
    }

  }

  private static class InventoryCounter {

    private final AtomicInteger items = new AtomicInteger(0);

    public void increment() {
      items.incrementAndGet();
    }

    public void decrement() {
      items.decrementAndGet();
    }

    public int getItems() {
      return items.get();
    }

  }

}
