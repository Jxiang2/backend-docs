package org.techdocs.interthread.simplecountdownlatch;

public class Demo {

  public static void main(final String[] args) throws InterruptedException {
    final SimpleCountDownLatch latch = new SimpleCountDownLatch(3);

    final Worker worker1 = new Worker(1000, latch, "Worker-1");
    final Worker worker2 = new Worker(2000, latch, "Worker-2");
    final Worker worker3 = new Worker(3000, latch, "Worker-3");

    worker1.start();
    worker2.start();
    worker3.start();

    latch.await(); // block until latch counted down to zero
    System.out.println("All workers finished");
  }

  static class Worker extends Thread {

    private final int delay;

    private final SimpleCountDownLatch latch;

    Worker(final int delay, final SimpleCountDownLatch latch, final String name) {
      super(name);
      this.delay = delay;
      this.latch = latch;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(delay);
        latch.countDown();
        System.out.println(Thread.currentThread().getName() + " finished");
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

}
