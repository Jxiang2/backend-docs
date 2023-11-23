package org.techdocs.performance;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Optimizing for Latency Part 2 - Image Processing
 */
public class ImageProcessing {

  public static final String SOURCE_FILE = "./resources/many-flowers.jpg";

  public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

  public static void main(final String[] args) throws IOException {

    final BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
    final BufferedImage resultImage = new BufferedImage(
        originalImage.getWidth(),
        originalImage.getHeight(),
        BufferedImage.TYPE_INT_RGB);

    final long startTime = System.currentTimeMillis();
    // recolorSingleThreaded(originalImage, resultImage);
    final int numberOfThreads = 1;
    recolorMultithreaded(originalImage, resultImage, numberOfThreads);
    final long endTime = System.currentTimeMillis();

    final long duration = endTime - startTime;

    final File outputFile = new File(DESTINATION_FILE);
    ImageIO.write(resultImage, "jpg", outputFile);

    System.out.println(duration);
  }

  public static void recolorMultithreaded(
      final BufferedImage originalImage,
      final BufferedImage resultImage,
      final int numberOfThreads) {
    final List<Thread> threads = new ArrayList<>();
    final int width = originalImage.getWidth();
    final int height = originalImage.getHeight() / numberOfThreads;

    // divide the image into numberOfThreads parts
    for (int i = 0; i < numberOfThreads; i++) {
      final int threadMultiplier = i;
      final Thread thread = new Thread(() -> {
        final int xOrigin = 0;
        final int yOrigin = height * threadMultiplier;
        recolorImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
      });

      threads.add(thread);
    }

    // start all threads
    for (final Thread thread : threads) {
      thread.start();
    }

    // wait for all threads to finish in parallel execution
    for (final Thread thread : threads) {
      try {
        thread.join();
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void recolorImage(
      final BufferedImage originalImage,
      final BufferedImage resultImage,
      final int leftCorner, final int topCorner,
      final int width, final int height) {
    for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
      for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
        recolorPixel(originalImage, resultImage, x, y);
      }
    }
  }

  public static void recolorPixel(
      final BufferedImage originalImage,
      final BufferedImage resultImage,
      final int x,
      final int y) {
    final int rgb = originalImage.getRGB(x, y);

    final int red = getRed(rgb);
    final int green = getGreen(rgb);
    final int blue = getBlue(rgb);

    final int newRed;
    final int newGreen;
    final int newBlue;

    if (isShadeOfGray(red, green, blue)) {
      newRed = Math.min(255, red + 10);
      newGreen = Math.max(0, green - 80);
      newBlue = Math.max(0, blue - 20);
    } else {
      newRed = red;
      newGreen = green;
      newBlue = blue;
    }
    final int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
    setRGB(resultImage, x, y, newRGB);
  }

  public static void setRGB(final BufferedImage image, final int x, final int y, final int rgb) {
    image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
  }

  public static boolean isShadeOfGray(final int red, final int green, final int blue) {
    return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
  }

  // build rgb from red, green, blue
  public static int createRGBFromColors(final int red, final int green, final int blue) {
    int rgb = 0;

    rgb |= blue;
    rgb |= green << 8;
    rgb |= red << 16;

    rgb |= 0xFF000000;

    return rgb;
  }

  public static int getRed(final int rgb) {
    return (rgb & 0x00FF0000) >> 16;
  }

  public static int getGreen(final int rgb) {
    return (rgb & 0x0000FF00) >> 8;
  }

  public static int getBlue(final int rgb) {
    return rgb & 0x000000FF;
  }

}
