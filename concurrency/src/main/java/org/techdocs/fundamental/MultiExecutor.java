package org.techdocs.fundamental;

import java.util.ArrayList;
import java.util.List;

public class MultiExecutor {

  private final List<Runnable> tasks;

  /*
   * @param tasks to executed concurrently
   */
  public MultiExecutor(final List<Runnable> tasks) {
    this.tasks = tasks;
  }

  /**
   * Executes all the tasks concurrently
   */
  public void executeAll() {
    final List<Thread> threads = new ArrayList<>(tasks.size());

    for (final Runnable task : tasks) {
      final Thread thread = new Thread(task);
      threads.add(thread);
    }

    for (final Thread thread : threads) {
      thread.start();
    }
  }

}