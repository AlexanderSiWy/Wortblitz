package ch.lu.beruf;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LevelGui extends BorderPane {

  private Level level;
  private Pane gameArea;
  private TextField input;
  private ImageView heart1;
  private ImageView heart2;
  private ImageView heart3;

  public LevelGui(Level level) {
    this.level = level;
    try {
      input = new TextField();
      setInputPropertys();
      setBottom(input);
      setBackground();
      gameArea = new Pane();
      setCenter(gameArea);
      AnchorPane menuPane = new AnchorPane();
      createMenubar(menuPane);
      setTop(menuPane);
    } catch (Exception e) {
      new ExceptionWarning(e);
    }
  }

  private void createMenubar(AnchorPane menuPane) {
    addCloseButton(menuPane);
    addHearts(menuPane);
  }

  private void addCloseButton(AnchorPane menuPane) {
    ImageView iVClose = new ImageView(new Image(Main.class.getResourceAsStream("images/close.png")));
    Button btnClose = new Button("", iVClose);
    iVClose.setFitHeight(20);
    iVClose.setFitWidth(20);
    btnClose.setBackground(null);
    btnClose.setOnAction(value -> {
      Main.toMainMenu();
      level.stopLevel();
    });
    menuPane.getChildren().add(btnClose);
    AnchorPane.setRightAnchor(btnClose, 10.0);
    AnchorPane.setTopAnchor(btnClose, 10.0);
  }

  private void addHearts(AnchorPane menuPane) {
    heart1 = new ImageView(new Image(Main.class.getResourceAsStream("images/heart.png")));
    heart1.setFitHeight(20);
    heart1.setFitWidth(20);
    heart2 = new ImageView(new Image(Main.class.getResourceAsStream("images/heart.png")));
    heart2.setFitHeight(20);
    heart2.setFitWidth(20);
    heart3 = new ImageView(new Image(Main.class.getResourceAsStream("images/heart.gif")));
    heart3.setFitHeight(20);
    heart3.setFitWidth(20);
    HBox hearts = new HBox(5, heart1, heart2, heart3);
    AnchorPane.setLeftAnchor(hearts, 10.0);
    AnchorPane.setTopAnchor(hearts, 10.0);
    menuPane.getChildren().add(hearts);
  }

  private void setInputPropertys() {
    input.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    input.setAlignment(Pos.CENTER);
    input.setFont(Font.loadFont(Main.class.getResourceAsStream("VT323-Regular.ttf"), 30));
    input.setStyle("-fx-text-inner-color: white;-fx-display-caret: false;-fx-highlight-fill: null;");
    input.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.TAB) {
        event.consume();
      }
    });
    input.textProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
      level.checkForKill(newValue);
      level.getPlayer().getPlayerScore().giveChange(oldValue, newValue);
      if (" ".equals(input.getText())) {
        input.setText("");
      }
    }));
  }

  private void setBackground() {
    BackgroundImage bgImage = new BackgroundImage(level.getBackgroundImage(), BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    setBackground(new Background(bgImage));
  }

  public Pane getPlayArea() {
    return gameArea;
  }

  public void addShip(Enemy enemy) {
    gameArea.getChildren().addAll(enemy.getImageView(), enemy.getText());
  }

  public void clearText() {
    input.clear();
  }

  public void removeHeart() {
    Player player = level.getPlayer();
    if (player.getHealth() == 2) {
      heart2.setImage(heart3.getImage());
      heart3.setImage(null);
    } else if (player.getHealth() == 1) {
      heart1.setImage(heart2.getImage());
      heart2.setImage(null);
    } else {
      heart1.setImage(null);
    }
  }

  public void deactivateInput() {
    input.setDisable(true);
  }

  public void showDeathScreen() {
    Platform.runLater(() -> {
      ImageView youDiedImage = new ImageView(new Image(Main.class.getResourceAsStream("images/youDied.png")));
      gameArea.getChildren().add(youDiedImage);
    });
  }

  public void showVictoryScreen() {
    Platform.runLater(new Runnable() {

      @Override
      public void run() {
        addVictoryImage();
        PlayerScore playerScore = level.getPlayer().getPlayerScore();
        addStatsText(playerScore);
        if (Main.isConnected() && isNewHightscore(playerScore)) {
          addNewHighscoreText();
        }
      }

      private void addVictoryImage() {
        ImageView vicotryImage = new ImageView(new Image(Main.class.getResourceAsStream("images/victory.png")));
        gameArea.getChildren().add(vicotryImage);
      }

      private void addStatsText(PlayerScore playerScore) {
        Text stats = new Text("Typed Characters: " + playerScore.getNumberOfCharactersTyped() + " Deleted Characters: "
            + playerScore.getDeletedCharacters() + " Total Score: " + playerScore.getScore());
        stats.setFont(Font.loadFont(Main.class.getResourceAsStream("VT323-Regular.ttf"), 30));
        stats.setY(250);
        stats.setX(100);
        gameArea.getChildren().add(stats);
      }

      private boolean isNewHightscore(PlayerScore playerScore) {
        String highscore = Main.getQuery().getHighscore(level.getLevelID(), Main.getPlayer());
        highscore = highscore.length() == 0 ? "0" : highscore;
        return Integer.parseInt(highscore) < playerScore.getScore();
      }

      private void addNewHighscoreText() {
        Text newHighscore = new Text("NEW HIGHSCORE!");
        newHighscore.setFont(Font.loadFont(Main.class.getResourceAsStream("VT323-Regular.ttf"), 50));
        newHighscore.setY(290);
        newHighscore.setX(350);
        gameArea.getChildren().add(newHighscore);
      }
    });
  }
}
