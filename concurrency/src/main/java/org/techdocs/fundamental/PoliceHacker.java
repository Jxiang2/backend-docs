package org.techdocs.fundamental;

import java.util.Random;
import java.util.Set;

public class PoliceHacker {

  private static final int MAX_PASSWORD_LENGTH = 5000;

  public static void main(final String[] args) {
    final Random random = new Random();
    final Vault vault = new Vault(random.nextInt(MAX_PASSWORD_LENGTH));

    final Set<Thread> threads = Set.of(
        new AscendingHackerThread(vault),
        new DescendingHackerThread(vault),
        new PoliceThread());

    threads.forEach(Thread::start);
  }

  private record Vault(int password) {

    public boolean isCorrectPassword(final int guess) {
      try {
        Thread.sleep(5);
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }
      return guess == this.password;
    }

  }

  // Hackers, accessing the vault
  private abstract static class HackerThread extends Thread {

    private final Vault vault;

    HackerThread(final Vault vault) {
      this.vault = vault;
      this.setName(this.getClass().getSimpleName());
      this.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void start() {
      System.out.println("Starting thread " + this.getName());
      super.start();
    }

    protected Vault getVault() {
      return vault;
    }

  }

  private static class AscendingHackerThread extends HackerThread {

    AscendingHackerThread(final Vault vault) {
      super(vault);
    }

    @Override
    public void run() {
      for (int guess = 0; guess < MAX_PASSWORD_LENGTH; guess++) {
        if (super.getVault().isCorrectPassword(guess)) {
          System.out.println(this.getName() + " guessed the password " + guess);
          System.exit(0);
        }
      }
    }

  }

  private static class DescendingHackerThread extends HackerThread {

    DescendingHackerThread(final Vault vault) {
      super(vault);
    }

    @Override
    public void run() {
      for (int guess = MAX_PASSWORD_LENGTH; guess >= 0; guess--) {
        if (super.getVault().isCorrectPassword(guess)) {
          System.out.println(this.getName() + " guessed the password " + guess);
          System.exit(0);
        }
      }
    }

  }

  // Polices
  private static class PoliceThread extends Thread {

    @Override
    public void run() {
      for (int i = 10; i > 0; i--) {
        try {
          Thread.sleep(1000);
        } catch (final InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(i + " seconds left");
      }
      System.out.println("Game over for you hackers");
      System.exit(0);
    }

  }

}
