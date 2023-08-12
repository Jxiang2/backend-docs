package org.techdocs.lockapi.cryptoboard;


import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.techdocs.lockapi.cryptoboard.backend.PriceUpdater;
import org.techdocs.lockapi.cryptoboard.backend.PricesContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * ReentrantLock Part 2 - User Interface Application example
 */
public class Frontend extends Application {

  public static void main(final String[] args) {
    launch(args);
  }

  @Override
  public void start(final Stage primaryStage) {
    primaryStage.setTitle("Cryptocurrency Prices");

    final GridPane grid = createGrid();
    final Map<String, Label> cryptoLabels = createCryptoPriceLabels();

    addLabelsToGrid(cryptoLabels, grid);

    final double width = 300;
    final double height = 250;

    final StackPane root = new StackPane();

    final Rectangle background = createBackgroundRectangleWithAnimation(width, height);

    root.getChildren().add(background);
    root.getChildren().add(grid);

    primaryStage.setScene(new Scene(root, width, height));

    final PricesContainer pricesContainer = new PricesContainer();

    final PriceUpdater priceUpdater = new PriceUpdater(pricesContainer);

    final AnimationTimer animationTimer = new AnimationTimer() {
      @Override
      public void handle(final long now) {
        if (pricesContainer.getLockObject().tryLock()) {
          try {
            final Label bitcoinLabel = cryptoLabels.get("BTC");
            bitcoinLabel.setText(String.valueOf(pricesContainer.getBitcoinPrice()));

            final Label etherLabel = cryptoLabels.get("ETH");
            etherLabel.setText(String.valueOf(pricesContainer.getEtherPrice()));

            final Label litecoinLabel = cryptoLabels.get("LTC");
            litecoinLabel.setText(String.valueOf(pricesContainer.getLitecoinPrice()));

            final Label bitcoinCashLabel = cryptoLabels.get("BCH");
            bitcoinCashLabel.setText(String.valueOf(pricesContainer.getBitcoinCashPrice()));

            final Label rippleLabel = cryptoLabels.get("XRP");
            rippleLabel.setText(String.valueOf(pricesContainer.getRipplePrice()));
          } finally {
            pricesContainer.getLockObject().unlock();
          }
        }
      }
    };


    // Set up the animation timer and price updater
    animationTimer.start();
    priceUpdater.start();

    // Add UI listeners
    addWindowResizeListener(primaryStage, background);

    // Show the stage
    primaryStage.show();
  }

  @Override
  public void stop() {
    System.exit(0);
  }

  private void addWindowResizeListener(final Stage stage, final Rectangle background) {
    final ChangeListener<Number> stageSizeListener = ((observable, oldValue, newValue) -> {
      background.setHeight(stage.getHeight());
      background.setWidth(stage.getWidth());
    });
    stage.widthProperty().addListener(stageSizeListener);
    stage.heightProperty().addListener(stageSizeListener);
  }

  private Map<String, Label> createCryptoPriceLabels() {
    final Label bitcoinPrice = new Label("0");
    bitcoinPrice.setId("BTC");

    final Label etherPrice = new Label("0");
    etherPrice.setId("ETH");

    final Label liteCoinPrice = new Label("0");
    liteCoinPrice.setId("LTC");

    final Label bitcoinCashPrice = new Label("0");
    bitcoinCashPrice.setId("BCH");

    final Label ripplePrice = new Label("0");
    ripplePrice.setId("XRP");

    final Map<String, Label> cryptoLabelsMap = new HashMap<>();
    cryptoLabelsMap.put("BTC", bitcoinPrice);
    cryptoLabelsMap.put("ETH", etherPrice);
    cryptoLabelsMap.put("LTC", liteCoinPrice);
    cryptoLabelsMap.put("BCH", bitcoinCashPrice);
    cryptoLabelsMap.put("XRP", ripplePrice);

    return cryptoLabelsMap;
  }

  private GridPane createGrid() {
    final GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setAlignment(Pos.CENTER);
    return grid;
  }

  private void addLabelsToGrid(final Map<String, Label> labels, final GridPane grid) {
    int row = 0;
    for (final Map.Entry<String, Label> entry : labels.entrySet()) {
      final String cryptoName = entry.getKey();
      final Label nameLabel = new Label(cryptoName);
      nameLabel.setTextFill(Color.BLUE);
      nameLabel.setOnMousePressed(event -> nameLabel.setTextFill(Color.RED));
      nameLabel.setOnMouseReleased((EventHandler) event -> nameLabel.setTextFill(Color.BLUE));

      grid.add(nameLabel, 0, row);
      grid.add(entry.getValue(), 1, row);

      row++;
    }
  }

  private Rectangle createBackgroundRectangleWithAnimation(final double width, final double height) {
    final Rectangle backround = new Rectangle(width, height);
    final FillTransition fillTransition =
      new FillTransition(Duration.millis(1000), backround, Color.LIGHTGREEN, Color.LIGHTBLUE);
    fillTransition.setCycleCount(Timeline.INDEFINITE);
    fillTransition.setAutoReverse(true);
    fillTransition.play();
    return backround;
  }

}