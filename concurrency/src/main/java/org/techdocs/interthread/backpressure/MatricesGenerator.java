package org.techdocs.interthread.backpressure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Objects as Condition Variables - Wait(), Notify() and NotifyAll()
 */
public class MatricesGenerator {

  private static final String OUTPUT_FILE = "./out/matrices";

  private static final int N = 10;

  private static final int NUMBER_OF_MATRIX_PAIRS = 100000;

  public static void main(final String[] args) throws IOException {
    final File file = new File(OUTPUT_FILE);
    final FileWriter fileWriter = new FileWriter(file);
    createMatrices(fileWriter);
    fileWriter.flush();
    fileWriter.close();
  }

  private static float[] createRow(final Random random) {
    final float[] row = new float[N];
    for (int i = 0; i < N; i++) {
      row[i] = random.nextFloat() * random.nextInt(100);
    }
    return row;
  }

  private static float[][] createMatrix(final Random random) {
    final float[][] matrix = new float[N][N];
    for (int i = 0; i < N; i++) {
      matrix[i] = createRow(random);
    }
    return matrix;
  }

  private static void saveMatrixToFile(
    final FileWriter fileWriter,
    final float[][] matrix
  ) throws IOException {
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

  private static void createMatrices(final FileWriter fileWriter) throws IOException {
    final Random random = new Random();
    for (int i = 0; i < NUMBER_OF_MATRIX_PAIRS * 2; i++) {
      final float[][] matrix = createMatrix(random);
      saveMatrixToFile(fileWriter, matrix);
    }
  }

}
