package org.techdocs.sharing;

/**
 * Resource Sharing & Introduction to Critical Sections
 */
public class CriticalSection {

  public static void main(final String[] args) throws InterruptedException {
    final InventoryCounter inventoryCounter = new InventoryCounter();
    final IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
    final DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

    // Start the threads in parallel
    incrementingThread.start();
    decrementingThread.start();

    incrementingThread.join();
    decrementingThread.join();

    // Start the threads sequentially
    // incrementingThread.start();
    // incrementingThread.join();
    //
    // decrementingThread.start();
    // decrementingThread.join();

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

    private int items = 0;

    public void increment() {
      items++;
    }

    public void decrement() {
      items--;
    }

    public int getItems() {
      return items;
    }

  }

}
