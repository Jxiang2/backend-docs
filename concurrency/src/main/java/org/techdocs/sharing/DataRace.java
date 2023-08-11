package org.techdocs.sharing;

/**
 * Data Races
 * // Way 1: Synchronize increment and checkForDataRace methods
 * // Way 2: Add Volatile to x and y (recommended)
 */
public class DataRace {

  public static void main(final String[] args) {
    final SharedClass sharedClass = new SharedClass();
    final Thread thread1 = new Thread(() -> {
      for (int i = 0; i < Integer.MAX_VALUE; i++) {
        sharedClass.increment();
      }
    });

    final Thread thread2 = new Thread(() -> {
      for (int i = 0; i < Integer.MAX_VALUE; i++) {
        sharedClass.checkForDataRace();
      }

    });

    thread1.start();
    thread2.start();
  }

  public static class SharedClass {

    private volatile int x = 0;

    private volatile int y = 0;

    public void increment() {
      x++;
      y++;
    }

    public void checkForDataRace() {
      // x should always be incremented before y
      // if not it's a data race
      if (y > x) {
        System.out.println("y > x - Data Race is detected");
      }
    }

  }

}

