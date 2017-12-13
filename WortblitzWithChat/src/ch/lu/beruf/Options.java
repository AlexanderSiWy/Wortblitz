package ch.lu.beruf;

import java.io.IOException;

import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Options extends GridPane {
  private Scene scene;
  private Label feedBack;
  private Label userCurrentlyLogedIn;
  private Label cityHighscore;
  private Label desertHighscore;
  private Label nightHighscore;
  private Label seaHighscore;
  private Label treeHighscore;
  private Label cityRank;
  private Label desertRank;
  private Label nightRank;
  private Label seaRank;
  private Label treeRank;
  private Label cityBestUser;
  private Label desertBestUser;
  private Label nightBestUser;
  private Label seaBestUser;
  private Label treeBestUser;
  private Label cityBestScore;
  private Label desertBestScore;
  private Label nightBestScore;
  private Label seaBestScore;
  private Label treeBestScore;
  private Label userCurrentlyInChat;
  private ChatClient chatClient;
  private TextField chatInput;

  public Options(Stage primaryStage) {
    super();
    initialiseGUI(primaryStage);
  }

  private void initialiseGUI(Stage primaryStage) {
    primaryStage.setWidth(780);
    primaryStage.setHeight(600);
    scene = new Scene(this);
    scene.getStylesheets().add(Main.class.getResource("MainMenu.css").toExternalForm());
    setHgap(5);
    setVgap(5);
    addAllGuiComponents();
    if (Main.isConnected()) {
      loadData();
    }
    chatInput.requestFocus();
  }

  private void addAllGuiComponents() {
    String fontPath = "VT323-Regular.ttf";
    addTitle(fontPath);
    addUserCurrentlyLogedIn(fontPath);
    addLoginRegisterLogout(fontPath);
    addHighscores(fontPath);
    addLevels(fontPath);
    addRanks(fontPath);
    addBestUser(fontPath);
    addBestScore(fontPath);
    addCloseButton();
    addChat(fontPath);
  }

  private void addChat(String fontPath) {
    userCurrentlyInChat = new Label();
    userCurrentlyInChat.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(userCurrentlyInChat, 0, 7);

    TextArea chatHistory = new TextArea();
    chatHistory.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    chatHistory.setEditable(false);
    add(chatHistory, 1, 7, 6, 1);

    chatInput = new TextField();
    chatInput.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(chatInput, 1, 8, 6, 1);

    try {
      if (Main.isConnected()) {
        chatInput.setOnAction(value -> {
          chatClient.sendMessage(chatInput.getText());
          chatInput.setText("");
        });
        chatClient = new ChatClient(chatHistory, userCurrentlyInChat);
      }
    } catch (IOException e) {
      new ExceptionWarning(e);
    }

  }

  private void addUserCurrentlyLogedIn(String fontPath) {
    userCurrentlyLogedIn = new Label("User: " + Main.getPlayer().getName());
    userCurrentlyLogedIn.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    userCurrentlyLogedIn.setPrefWidth(190);
    add(userCurrentlyLogedIn, 5, 1);
  }

  private void addTitle(String fontPath) {
    Label title = new Label("Wortblitz");
    title.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 50));
    add(title, 0, 0, 5, 1);
  }

  private void addBestScore(String fontPath) {
    Label bestScore = new Label("Best Score:");
    bestScore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    bestScore.setPrefWidth(100);
    add(bestScore, 2, 1);

    cityBestScore = new Label();
    cityBestScore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(cityBestScore, 2, 2);

    desertBestScore = new Label();
    desertBestScore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(desertBestScore, 2, 3);

    nightBestScore = new Label();
    nightBestScore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(nightBestScore, 2, 4);

    seaBestScore = new Label();
    seaBestScore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(seaBestScore, 2, 5);

    treeBestScore = new Label();
    treeBestScore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(treeBestScore, 2, 6);
  }

  private void addBestUser(String fontPath) {
    Label bestUser = new Label("Best User:");
    bestUser.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    bestUser.setPrefWidth(150);
    add(bestUser, 1, 1);

    cityBestUser = new Label();
    cityBestUser.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(cityBestUser, 1, 2);

    desertBestUser = new Label();
    desertBestUser.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(desertBestUser, 1, 3);

    nightBestUser = new Label();
    nightBestUser.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(nightBestUser, 1, 4);

    seaBestUser = new Label();
    seaBestUser.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(seaBestUser, 1, 5);

    treeBestUser = new Label();
    treeBestUser.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(treeBestUser, 1, 6);
  }

  private void addRanks(String fontPath) {
    Label rank = new Label("Your Rank:");
    rank.setPrefWidth(100);
    rank.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(rank, 3, 1);

    cityRank = new Label();
    cityRank.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(cityRank, 3, 2);

    desertRank = new Label();
    desertRank.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(desertRank, 3, 3);

    nightRank = new Label();
    nightRank.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(nightRank, 3, 4);

    seaRank = new Label();
    seaRank.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(seaRank, 3, 5);

    treeRank = new Label();
    treeRank.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(treeRank, 3, 6);
  }

  private void addLevels(String fontPath) {
    Label level = new Label("Level:");
    level.setPrefWidth(90);
    level.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(level, 0, 1);

    Label city = new Label("City: ");
    city.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(city, 0, 2);

    Label desert = new Label("Desert:");
    desert.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(desert, 0, 3);

    Label night = new Label("Night:");
    night.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(night, 0, 4);

    Label sea = new Label("Sea:");
    sea.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(sea, 0, 5);

    Label tree = new Label("Tree:");
    tree.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(tree, 0, 6);
  }

  private void addHighscores(String fontPath) {
    Label highscore = new Label("Highscore:");
    highscore.setPrefWidth(100);
    highscore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(highscore, 4, 1);

    cityHighscore = new Label();
    cityHighscore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(cityHighscore, 4, 2);

    desertHighscore = new Label();
    desertHighscore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(desertHighscore, 4, 3);

    nightHighscore = new Label();
    nightHighscore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(nightHighscore, 4, 4);

    seaHighscore = new Label();
    seaHighscore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(seaHighscore, 4, 5);

    treeHighscore = new Label();
    treeHighscore.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(treeHighscore, 4, 6);
  }

  private void addCloseButton() {
    ImageView iVClose = new ImageView(new Image(Main.class.getResourceAsStream("images/close.png")));
    Button btnClose = new Button("", iVClose);
    iVClose.setFitHeight(20);
    iVClose.setFitWidth(20);
    btnClose.setBackground(null);
    btnClose.setOnAction(value -> {
      if (Main.isConnected()) {
        chatClient.exit();
      }
      Main.toMainMenu();
    });
    GridPane.setHalignment(btnClose, HPos.RIGHT);
    add(btnClose, 5, 6);
  }

  private void addLoginRegisterLogout(String fontPath) {
    TextField userName = new TextField();
    userName.setPromptText("Username");
    userName.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(userName, 5, 2);

    TextField password = new TextField();
    password.setPromptText("Password");
    userName.setOnAction(value -> password.requestFocus());
    password.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(password, 5, 3);

    String btnBorderColor = "-fx-border-color: lightblue;";

    Button btnLogIn = new Button("Log In");
    password.setOnAction(value -> logIn(userName.getText(), password.getText()));
    btnLogIn.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    btnLogIn.setOnAction(value -> logIn(userName.getText(), password.getText()));
    btnLogIn.setBackground(null);
    btnLogIn.setStyle(btnBorderColor);
    btnLogIn.setPrefWidth(90);
    add(btnLogIn, 5, 4);

    Button btnLogOut = new Button("Log Out");
    btnLogOut.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    btnLogOut.setOnAction(value -> {
      Main.setPlayer(new Player("Guest", 1));
      loadData();
      feedBack.setText("Log Out Ok");
    });
    btnLogOut.setBackground(null);
    btnLogOut.setStyle(btnBorderColor);
    btnLogOut.setPrefWidth(90);
    GridPane.setHalignment(btnLogOut, HPos.RIGHT);
    add(btnLogOut, 5, 4);

    Button btnRegister = new Button("Register");
    btnRegister.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    btnRegister.setOnAction(value -> register(userName.getText(), password.getText()));
    btnRegister.setBackground(null);
    btnRegister.setStyle(btnBorderColor);
    btnRegister.setPrefWidth(190);
    add(btnRegister, 5, 5);

    feedBack = new Label();
    feedBack.setFont(Font.loadFont(Main.class.getResourceAsStream(fontPath), 20));
    add(feedBack, 5, 6);
  }

  private void register(String userName, String password) {
    if (Main.isConnected()) {
      if (userName.length() <= 30 && userName.length() != 0) {
        if (password.length() <= 30 && password.length() != 0) {
          Player player = Main.getQuery().register(userName, password);
          if (player != null) {
            Main.setPlayer(player);
            loadData();
            feedBack.setText("Register OK");
          } else {
            feedBack.setText("Register failed");
          }
        } else {
          feedBack.setText("Passwort ungültig");
        }
      } else {
        feedBack.setText("Name ungültig");
      }
    } else {
      feedBack.setText("Not connected");
    }
  }

  private void logIn(String userName, String password) {
    if (Main.isConnected()) {
      Player player = Main.getQuery().logIn(userName, password);
      if (player != null) {
        Main.setPlayer(player);
        loadData();
        feedBack.setText("Log In OK");
      } else {
        feedBack.setText("Log In failed");
      }
    } else {
      feedBack.setText("Not connected");
    }
  }

  private void loadData() {
    userCurrentlyLogedIn.setText("User: " + Main.getPlayer().getName());
    treeHighscore.setText(Main.getQuery().getHighscore(5, Main.getPlayer()));
    seaHighscore.setText(Main.getQuery().getHighscore(4, Main.getPlayer()));
    nightHighscore.setText(Main.getQuery().getHighscore(3, Main.getPlayer()));
    desertHighscore.setText(Main.getQuery().getHighscore(2, Main.getPlayer()));
    cityHighscore.setText(Main.getQuery().getHighscore(1, Main.getPlayer()));
    treeRank.setText(Main.getQuery().getRank(5, treeHighscore.getText()));
    seaRank.setText(Main.getQuery().getRank(4, seaHighscore.getText()));
    nightRank.setText(Main.getQuery().getRank(3, nightHighscore.getText()));
    desertRank.setText(Main.getQuery().getRank(2, desertHighscore.getText()));
    cityRank.setText(Main.getQuery().getRank(1, cityHighscore.getText()));

    String[] bestPlayerAndScoreCity = Main.getQuery().getBestPlayerAndScore(1);
    cityBestUser.setText(bestPlayerAndScoreCity[0]);
    cityBestScore.setText(bestPlayerAndScoreCity[1]);
    String[] bestPlayerAndScoreDesert = Main.getQuery().getBestPlayerAndScore(2);
    desertBestUser.setText(bestPlayerAndScoreDesert[0]);
    desertBestScore.setText(bestPlayerAndScoreDesert[1]);
    String[] bestPlayerAndScoreNight = Main.getQuery().getBestPlayerAndScore(3);
    nightBestUser.setText(bestPlayerAndScoreNight[0]);
    nightBestScore.setText(bestPlayerAndScoreNight[1]);
    String[] bestPlayerAndScoreSea = Main.getQuery().getBestPlayerAndScore(4);
    seaBestUser.setText(bestPlayerAndScoreSea[0]);
    seaBestScore.setText(bestPlayerAndScoreSea[1]);
    String[] bestPlayerAndScoreTree = Main.getQuery().getBestPlayerAndScore(5);
    treeBestUser.setText(bestPlayerAndScoreTree[0]);
    treeBestScore.setText(bestPlayerAndScoreTree[1]);

    chatClient.setPlayer();
  }

  public Scene getOptionScene() {
    return scene;
  }
}
