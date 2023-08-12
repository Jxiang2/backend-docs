package org.techdocs.lockapi;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Reentrant Read Write Lock & Database Implementation
 * Use readWriteLock: 1327 ms
 * Use lock: 274 ms
 */
public class ReadWriteInventory {

  public static final int HIGHEST_PRICE = 1000;

  public static void main(final String[] args) throws InterruptedException {
    final InventoryDatabase inventoryDatabase = new InventoryDatabase();

    final Random random = new Random();
    for (int i = 0; i < 100000; i++) {
      inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
    }

    final Thread writer = new Thread(() -> {
      while (true) {
        inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
        try {
          Thread.sleep(10);
        } catch (final InterruptedException ignored) {
        }
      }
    });

    writer.setDaemon(true);
    writer.start();

    final int numberOfReaderThreads = 7;
    final List<Thread> readers = new ArrayList<>();

    for (int readerIndex = 0; readerIndex < numberOfReaderThreads; readerIndex++) {
      final Thread reader = new Thread(() -> {
        for (int i = 0; i < 100000; i++) {
          final int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
          final int lowerBoundPrice = upperBoundPrice > 0
            ? random.nextInt(upperBoundPrice)
            : 0;
          final int numberOfItemsInPriceRange =
            inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
        }
      });

      reader.setDaemon(true);
      readers.add(reader);
    }

    final long startReadingTime = System.currentTimeMillis();
    for (final Thread reader : readers) {
      reader.start();
    }

    for (final Thread reader : readers) {
      reader.join();
    }

    final long endReadingTime = System.currentTimeMillis();

    System.out.printf("Reading took %d ms%n", endReadingTime - startReadingTime);
  }

  public static class InventoryDatabase {

    private final TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = reentrantReadWriteLock.readLock();

    private final Lock writeLock = reentrantReadWriteLock.writeLock();

    private final Lock lock = new ReentrantLock();

    public int getNumberOfItemsInPriceRange(final int lowerPriceBound, final int upperPriceBound) {
      //      lock.lock();
      readLock.lock();
      try {
        final Integer fromKey = priceToCountMap.ceilingKey(lowerPriceBound);

        final Integer toKey = priceToCountMap.floorKey(upperPriceBound);

        if (fromKey == null || toKey == null) {
          return 0;
        }

        final NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(
          fromKey, true, toKey, true
        );

        int sum = 0;
        for (final int numberOfItemsForPrice : rangeOfPrices.values()) {
          sum += numberOfItemsForPrice;
        }

        return sum;

      } finally {
        readLock.unlock();
        //        lock.unlock();
      }
    }

    public void addItem(final int price) {
      //      lock.lock();
      writeLock.lock();
      try {
        final Integer numberOfItemsForPrice = priceToCountMap.get(price);
        if (numberOfItemsForPrice == null) {
          priceToCountMap.put(price, 1);
        } else {
          priceToCountMap.put(price, numberOfItemsForPrice + 1);
        }

      } finally {
        writeLock.unlock();
        //        lock.unlock();
      }
    }

    public void removeItem(final int price) {
      //      lock.lock();
      writeLock.lock();
      try {
        final Integer numberOfItemsForPrice = priceToCountMap.get(price);
        if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
          priceToCountMap.remove(price);
        } else {
          priceToCountMap.put(price, numberOfItemsForPrice - 1);
        }
      } finally {
        writeLock.unlock();
        //        lock.unlock();
      }
    }

  }

}
