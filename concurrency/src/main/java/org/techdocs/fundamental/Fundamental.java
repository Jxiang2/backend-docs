package org.techdocs.fundamental;

/**
 * Create thread
 * Debug thread code
 */
public class Fundamental {

  public static void main(final String[] args) throws InterruptedException {
    final Thread t = new Thread(() -> {
      System.out.println("In the new thread: " + Thread.currentThread().getName());
      System.out.println(Thread.currentThread().getPriority());

      // this will trigger the uncaught exception handler
      throw new RuntimeException("Intentional exception");
    });
    t.setName("worker");
    t.setPriority(Thread.MAX_PRIORITY);
    t.setUncaughtExceptionHandler((thread, exception) -> {
      System.out.println("Exception in thread: " + thread.getName() + " " + exception.getMessage());
    });

    System.out.println("Before thread starts: " + Thread.currentThread().getName());
    t.start();
    System.out.println("After thread starts: " + Thread.currentThread().getName());

    Thread.sleep(1000);
  }

}
