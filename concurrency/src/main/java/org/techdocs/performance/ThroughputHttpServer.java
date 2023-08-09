package org.techdocs.performance;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Optimizing for Throughput Part 2 - HTTP server + Jmeter
 */
public class ThroughputHttpServer {

  private static final String INPUT_FILE = "./resources/war_and_peace.txt";

  private static final int NUMBER_OF_THREADS = 8;

  public static void main(final String[] args) throws IOException {
    final String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
    startServer(text);
  }

  public static void startServer(final String text) throws IOException {
    final HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/search", new WordCountHandler(text));

    final Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    server.setExecutor(executor);
    server.start();
  }

  private static class WordCountHandler implements HttpHandler {

    private final String text;

    WordCountHandler(final String text) {
      this.text = text;
    }

    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {
      final String query = httpExchange.getRequestURI().getQuery();
      final String[] keyValue = query.split("=");
      final String action = keyValue[0];
      final String word = keyValue[1];
      if (!action.equals("word")) {
        httpExchange.sendResponseHeaders(400, 0);
        return;
      }

      final long count = countWord(word);

      final byte[] response = Long.toString(count).getBytes();
      httpExchange.sendResponseHeaders(200, response.length);
      final OutputStream outputStream = httpExchange.getResponseBody();
      outputStream.write(response);
      outputStream.close();
    }

    private long countWord(final String word) {
      long count = 0;
      int index = 0;
      while (index >= 0) {
        index = text.indexOf(word, index);

        if (index >= 0) {
          count++;
          index++;
        }
      }
      return count;
    }

  }

}
