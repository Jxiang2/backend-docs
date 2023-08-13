package org.techdocs.interthread.backpressure;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Objects as Condition Variables - Wait(), Notify() and NotifyAll()
 */
public class BackPressureDemo {

  private static final String INPUT_FILE = "./out/matrices";

  private static final String OUTPUT_FILE = "./out/matrices_results.txt";

  private static final int N = 10;

  public static void main(final String[] args) throws IOException {
    final ThreadSafeQueue<MatricesPair> threadSafeQueue = new ThreadSafeQueue<>();
    final File inputFile = new File(INPUT_FILE);
    final File outputFile = new File(OUTPUT_FILE);

    final MatricesReaderProducer matricesReader =
      new MatricesReaderProducer(new FileReader(inputFile), threadSafeQueue);
    final MatricesMultiplierConsumer matricesConsumer1 =
      new MatricesMultiplierConsumer(new FileWriter(outputFile), threadSafeQueue);
    final MatricesMultiplierConsumer matricesConsumer2 =
      new MatricesMultiplierConsumer(new FileWriter(outputFile), threadSafeQueue);

    matricesConsumer1.start();
    matricesConsumer2.start();

    matricesReader.start();
  }

  private static class MatricesMultiplierConsumer extends Thread {

    private final ThreadSafeQueue<MatricesPair> queue;

    private final FileWriter fileWriter;

    MatricesMultiplierConsumer(final FileWriter fileWriter, final ThreadSafeQueue<MatricesPair> queue) {
      this.fileWriter = fileWriter;
      this.queue = queue;
    }

    private static void saveMatrixToFile(final FileWriter fileWriter, final float[][] matrix) throws IOException {
      for (int r = 0; r < N; r++) {
        final StringJoiner stringJoiner = new StringJoiner(", ");
        for (int c = 0; c < N; c++) {
          stringJoiner.add(String.format("%.2f", matrix[r][c]));
        }
        fileWriter.write(stringJoiner.toString());
        fileWriter.write('\n');
      }
      fileWriter.write('\n');
    }

    @Override
    public void run() {
      while (true) {
        final MatricesPair matricesPair = queue.remove();
        if (matricesPair == null) {
          break;
        }

        final float[][] result = multiplyMatrices(matricesPair.matrix1, matricesPair.matrix2);

        try {
          saveMatrixToFile(fileWriter, result);
        } catch (final IOException ignored) {
        }
      }

      try {
        fileWriter.flush();
        fileWriter.close();
      } catch (final IOException ignored) {
      }
    }

    private float[][] multiplyMatrices(final float[][] m1, final float[][] m2) {
      final float[][] result = new float[N][N];
      for (int r = 0; r < N; r++) {
        for (int c = 0; c < N; c++) {
          for (int k = 0; k < N; k++) {
            result[r][c] += m1[r][k] * m2[k][c];
          }
        }
      }
      return result;
    }

  }

  private static class MatricesReaderProducer extends Thread {

    private final Scanner scanner;

    private final ThreadSafeQueue<MatricesPair> queue;

    MatricesReaderProducer(final FileReader reader, final ThreadSafeQueue<MatricesPair> queue) {
      this.scanner = new Scanner(reader);
      this.queue = queue;
    }

    @Override
    public void run() {
      while (true) {
        final float[][] matrix1 = readMatrix();
        final float[][] matrix2 = readMatrix();

        if (matrix1 == null || matrix2 == null) {
          System.out.println("No more matrices in the file. Producer Thread is terminating...");
          queue.terminate();
          return;
        }

        final MatricesPair matricesPair = new MatricesPair();
        matricesPair.matrix1 = matrix1;
        matricesPair.matrix2 = matrix2;

        queue.add(matricesPair);
      }
    }

    private float[][] readMatrix() {
      final float[][] matrix = new float[N][N];
      for (int r = 0; r < N; r++) {
        if (!scanner.hasNext()) {
          return null;
        }
        final String[] line = scanner.nextLine().split(",");
        for (int c = 0; c < N; c++) {
          matrix[r][c] = Float.parseFloat(line[c]);
        }
      }
      scanner.nextLine();
      return matrix;
    }

  }


  private static class ThreadSafeQueue<T> {

    private static final int CAPACITY = 5;

    private final Queue<T> queue = new LinkedList<>();

    private boolean isEmpty = true;

    private boolean isTerminate = false;


    // Producer adds matrices to the queue
    public synchronized void add(final T item) {
      while (queue.size() == CAPACITY) {
        try {
          wait();
        } catch (final InterruptedException ignored) {
        }
      }

      queue.add(item);
      isEmpty = false;
      notify(); // notify the single consumer thread that there are new matrices to consume
    }

    // Consumer removes matrices from the queue
    public synchronized T remove() {
      T item = null;
      while (isEmpty && !isTerminate) {
        try {
          wait(); // Nothing to consume, sleep and wait for the producer to produce
        } catch (final InterruptedException ignored) {
        }
      }

      final int currentSize = queue.size();

      if (currentSize == 1) {
        isEmpty = true;
      }

      if (currentSize == 0 && isTerminate) {
        return null;
      }

      System.out.println("queue size " + queue.size());

      item = queue.remove();
      final int newSize = queue.size();
      if (currentSize == 5 && newSize == 4) {
        notifyAll(); // notify all producer threads that there is space in the queue
      }
      return item;
    }

    // Producer let the consumer know that there are no more matrices to read and to terminate the thread
    public synchronized void terminate() {
      isTerminate = true;
      notifyAll();
    }

  }

  private static class MatricesPair {

    private float[][] matrix1;

    private float[][] matrix2;

    public float[][] getMatrix1() {
      return matrix1;
    }

    public void setMatrix1(final float[][] matrix1) {
      this.matrix1 = matrix1;
    }

    public float[][] getMatrix2() {
      return matrix2;
    }

    public void setMatrix2(final float[][] matrix2) {
      this.matrix2 = matrix2;
    }

  }

}
