package org.techdocs.fundamental;

/**
 * Create thread
 * Debug thread code: break point --> Alt + 5 --> select "Threads"
 */
public class Fundamental {

  public static void main(final String[] args) throws InterruptedException {
    // Way 1
    final Thread t = new Thread(() -> {
      System.out.println(
        "In the new thread: " +
          Thread.currentThread().getName() +
          " with priority: " +
          Thread.currentThread().getPriority()
      );
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

    // Way 2
    final Thread t2 = new ThreadClass();
    t2.start();

  }

  // Way 2
  private static class ThreadClass extends Thread {

    @Override
    public void run() {
      System.out.println("In the new thread: " + this.getName());
      System.out.println(this.getPriority());
    }

  }

}
