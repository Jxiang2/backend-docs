package org.techdocs.sharing;

/**
 * Critical Section & Synchronization
 */
public class Sync {

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
      for (int i = 0; i < 5000; i++) {
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

    private final Object lock = new Object();

    private int items = 0;

    public void increment() {
      synchronized (this.lock) { // synchronized(this)
        items++;
      }
    }

    public void decrement() {
      synchronized (this.lock) {
        items--;
      }
    }

    public int getItems() {
      synchronized (this.lock) {
        return items;
      }
    }

  }

}
