package org.techdocs.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * Atomic References, Compare And Set, Lock-Free High Performance Data Structure
 */
public class AtomicReferences {

  public static void main(final String[] args) throws InterruptedException {
    //    final StandardStack<Integer> stack = new StandardStack<>();
    final LockFreeStack<Integer> stack = new LockFreeStack<>();
    final Random random = new Random();

    for (int i = 0; i < 100000; i++) {
      stack.push(random.nextInt());
    }

    final List<Thread> threads = new ArrayList<>();

    final int pushingThreads = 2;
    final int poppingThreads = 2;

    for (int i = 0; i < pushingThreads; i++) {
      final Thread thread = new Thread(() -> {
        while (true) {
          stack.push(random.nextInt());
        }
      });

      thread.setDaemon(true);
      threads.add(thread);
    }

    for (int i = 0; i < poppingThreads; i++) {
      final Thread thread = new Thread(() -> {
        while (true) {
          final Integer pop = stack.pop();
        }
      });

      thread.setDaemon(true);
      threads.add(thread);
    }

    for (final Thread thread : threads) {
      thread.start();
    }

    Thread.sleep(10000);

    System.out.printf(
      "%,d operations were performed in 10 seconds %n", stack.getCounter()
    );
  }

  public static class LockFreeStack<T> {

    private final AtomicReference<StackNode<T>> head = new AtomicReference<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public void push(final T value) {
      final StackNode<T> newHeadNode = new StackNode<>(value);

      while (true) {
        final StackNode<T> currentHeadNode = this.head.get();
        newHeadNode.next = currentHeadNode;
        if (this.head.compareAndSet(currentHeadNode, newHeadNode)) {
          break;
        } else {
          // this.head is changed by another thread, wait for 1 nanosecond, and try the loop again
          LockSupport.parkNanos(1);
        }
      }

      counter.incrementAndGet();
    }

    public T pop() {
      StackNode<T> currentHeadNode = this.head.get();
      StackNode<T> newHeadNode;

      while (currentHeadNode != null) {
        newHeadNode = currentHeadNode.next;
        if (this.head.compareAndSet(currentHeadNode, newHeadNode)) {
          break;
        } else {
          // this.head is changed by another thread after read it. wait for 1 nanosecond, and try the loop again
          LockSupport.parkNanos(1);
          currentHeadNode = this.head.get();
        }
      }

      counter.incrementAndGet();

      return currentHeadNode != null ? currentHeadNode.value : null;
    }

    public int getCounter() {
      return counter.get();
    }

  }

  public static class StandardStack<T> {

    private StackNode<T> head;

    private int counter = 0;

    public synchronized void push(final T value) {
      final StackNode<T> newHead = new StackNode<>(value);
      newHead.next = this.head; // possible race condition, this.head might be changed by another thread
      this.head = newHead; // possible race condition, this.head might be changed by another thread
      counter++; // possible race condition
    }

    public synchronized T pop() {
      if (head == null) {
        counter++;
        return null;
      }

      final T value = this.head.value; // possible race condition, this.head might be changed by another thread
      this.head = this.head.next; // possible race condition, this.head might be changed by another thread
      counter++; // possible race condition
      return value;
    }

    public int getCounter() {
      return counter;
    }

  }

  private static class StackNode<T> {

    private final T value;

    private StackNode<T> next;

    StackNode(final T value) {
      this.value = value;
    }

  }

}
