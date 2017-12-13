package ch.lu.beruf;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

  public static final int WIDTH = 900;
  public static final int HEIGHT = 500;
  private static Stage primaryStage;
  private static Scene scene;
  private static Player player;
  private static Label userName;
  private static Query query;
  private static boolean isConnected = false;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    startWortblitz(primaryStage);
  }

  private static void startWortblitz(Stage primaryStage) {
    try {
      query = new Query();
      isConnected = query.connect();
      Main.primaryStage = primaryStage;

      player = Player.loadPlayer();

      initializeGUI();
      Main.primaryStage.show();
    } catch (Exception e) {
      new ExceptionWarning(e);
    }
  }

  private static void initializeGUI() {
    primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/normalEnemy.png")));
    if (Main.isConnected()) {
      primaryStage.setOnCloseRequest(value -> query.closeConnection());
    }
    GridPane root = new GridPane();

    Label title = new Label("Wortblitz");
    String fontPath = "VT323-Regular.ttf";
    title.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 50));
    root.add(title, 0, 0);

    userName = new Label();
    userName.setText("User: " + player.getName());
    userName.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    GridPane.setHalignment(userName, HPos.RIGHT);
    root.add(userName, 2, 0);

    int imageHeight = 111;
    int imageWidth = 200;

    createLevelCity(imageHeight, imageWidth, root);

    createLevelDesert(imageHeight, imageWidth, root);

    createLevelNight(imageHeight, imageWidth, root);

    createLevelSea(imageHeight, imageWidth, root);

    createLevelTree(imageHeight, imageWidth, root);

    createOptions(imageHeight, imageWidth, root);

    scene = new Scene(root);
    scene.getStylesheets().add(Main.class.getResource("MainMenu.css").toExternalForm());
    primaryStage.setWidth(664);
    primaryStage.setHeight(350);
    primaryStage.setResizable(false);
    Main.primaryStage.setScene(scene);
  }

  public static void toMainMenu() {
    primaryStage.setScene(scene);
    primaryStage.setWidth(664);
    primaryStage.setHeight(350);
    userName.setText("User: " + player.getName());
  }

  public static Player getPlayer() {
    return player;
  }

  public static void setPlayer(Player player) {
    Main.player = player;
    if (Main.isConnected()) {
      Main.player.savePlayer();
    }
  }

  private static void startLevel(int levelID, Image image, BossData bossData, String words, Difficulty difficulty) {
    player.setFullHealth();
    player.getPlayerScore().resetScore();
    Level level = new Level(levelID, image, bossData, words, difficulty);
    Thread levelThread = new Thread(level);
    Scene levelScene = new Scene(level.getLevelGui());
    primaryStage.setScene(levelScene);
    primaryStage.setWidth((double) WIDTH + 6);
    primaryStage.setHeight((double) HEIGHT + 28);
    levelThread.setDaemon(true);
    levelThread.start();
  }

  private static void createOptions(int imageHeight, int imageWidth, GridPane root) {
    Image imageOptions = new Image(Main.class.getResourceAsStream("images/options.png"));
    ImageView imageViewOptions = new ImageView(imageOptions);
    imageViewOptions.setFitHeight(imageHeight);
    imageViewOptions.setFitWidth(imageWidth);
    Button btnOptions = new Button("", imageViewOptions);
    btnOptions.setOnAction(value -> {
      Options options = new Options(primaryStage);
      primaryStage.setScene(options.getOptionScene());
    });
    root.add(btnOptions, 2, 2);
  }

  private static void createLevelTree(int imageHeight, int imageWidth, GridPane root) {
    Image imageTree = new Image(Main.class.getResourceAsStream("images/backgroundTree.png"));
    ImageView imageViewTree = new ImageView(imageTree);
    imageViewTree.setFitHeight(imageHeight);
    imageViewTree.setFitWidth(imageWidth);
    Button btnTree = new Button("", imageViewTree);
    btnTree.setOnAction(value -> {
      Image bossImageTree = new Image(Main.class.getResourceAsStream("images/bossEnemy.png"));
      String wordsTree = "words/words.txt";
      String wordsBoss = "words/bossWords.txt";
      BossDifficutly bossDifficultyTree = new BossDifficutly(1, 0, 2500, 0.1);
      BossData bossDataTree = new BossData("Ouu", bossImageTree, wordsBoss, bossDifficultyTree);
      Difficulty difficultyTree = new Difficulty(20, 3000, 100);
      startLevel(5, imageTree, bossDataTree, wordsTree, difficultyTree);
    });
    root.add(btnTree, 1, 2);
  }

  private static void createLevelSea(int imageHeight, int imageWidth, GridPane root) {
    Image imageSea = new Image(Main.class.getResourceAsStream("images/backgroundSea.png"));
    ImageView imageViewSea = new ImageView(imageSea);
    imageViewSea.setFitHeight(imageHeight);
    imageViewSea.setFitWidth(imageWidth);
    Button btnSea = new Button("", imageViewSea);
    btnSea.setOnAction(value -> {
      String wordsSea = "words/words.txt";
      String wordsBoss = "words/bossWords.txt";
      Image bossImageSea = new Image(Main.class.getResourceAsStream("images/bossEnemy.png"));
      BossDifficutly bossDifficultySea = new BossDifficutly(1, 0, 2500, 0.1);
      BossData bossDataSea = new BossData("Ouu", bossImageSea, wordsBoss, bossDifficultySea);
      Difficulty difficultySea = new Difficulty(20, 3000, 100);
      startLevel(4, imageSea, bossDataSea, wordsSea, difficultySea);
    });
    root.add(btnSea, 0, 2);
  }

  private static void createLevelNight(int imageHeight, int imageWidth, GridPane root) {
    Image imageNight = new Image(Main.class.getResourceAsStream("images/backgroundNight.png"));
    ImageView imageViewNight = new ImageView(imageNight);
    imageViewNight.setFitHeight(imageHeight);
    imageViewNight.setFitWidth(imageWidth);
    Button btnNight = new Button("", imageViewNight);
    btnNight.setOnAction(value -> {
      String wordsNight = "words/words.txt";
      String wordsBoss = "words/bossWords.txt";
      Image bossImageNight = new Image(Main.class.getResourceAsStream("images/bossEnemy.png"));
      BossDifficutly bossDifficultyNight = new BossDifficutly(1, 0, 2500, 0.1);
      BossData bossDataNight = new BossData("Ouu", bossImageNight, wordsBoss, bossDifficultyNight);
      Difficulty difficultyNight = new Difficulty(20, 3000, 100);
      startLevel(3, imageNight, bossDataNight, wordsNight, difficultyNight);
    });
    root.add(btnNight, 2, 1);
  }

  private static void createLevelDesert(int imageHeight, int imageWidth, GridPane root) {
    Image imageDesert = new Image(Main.class.getResourceAsStream("images/backgroundDesert.png"));
    ImageView imageViewDesert = new ImageView(imageDesert);
    imageViewDesert.setFitHeight(imageHeight);
    imageViewDesert.setFitWidth(imageWidth);
    Button btnDesert = new Button("", imageViewDesert);
    btnDesert.setOnAction(value -> {
      Difficulty difficultyDesert = new Difficulty(2, 3000, 100);
      String wordsDesert = "words/words.txt";
      String wordsBoss = "words/bossWords.txt";
      Image bossImageDesert = new Image(Main.class.getResourceAsStream("images/bossEnemy.png"));
      BossDifficutly bossDifficultyDesert = new BossDifficutly(1, 0, 2500, 0.1);
      BossData bossDataDesert = new BossData("Ouu", bossImageDesert, wordsBoss, bossDifficultyDesert);
      startLevel(2, imageDesert, bossDataDesert, wordsDesert, difficultyDesert);
    });
    root.add(btnDesert, 1, 1);
  }

  private static void createLevelCity(int imageHeight, int imageWidth, GridPane root) {
    Image imageCity = new Image(Main.class.getResourceAsStream("images/backgroundCity.png"));
    ImageView imageViewCity = new ImageView(imageCity);
    imageViewCity.setFitHeight(imageHeight);
    imageViewCity.setFitWidth(imageWidth);
    Button btnCity = new Button("", imageViewCity);
    btnCity.setOnAction(value -> {
      String wordsCity = "words/words.txt";
      String wordsBoss = "words/bossWords.txt";
      Image bossImageCity = new Image(Main.class.getResourceAsStream("images/bossEnemy.png"));
      BossDifficutly bossDifficultyCity = new BossDifficutly(1, 0, 2500, 0.1);
      BossData bossDataCity = new BossData("Haleluia gepriesen sei Gott", bossImageCity, wordsBoss, bossDifficultyCity);
      Difficulty difficultyCity = new Difficulty(20, 3000, 100);
      startLevel(1, imageCity, bossDataCity, wordsCity, difficultyCity);
    });
    root.add(btnCity, 0, 1);
  }

  public static Query getQuery() {
    return query;
  }

  public static boolean isConnected() {
    return isConnected;
  }
}
