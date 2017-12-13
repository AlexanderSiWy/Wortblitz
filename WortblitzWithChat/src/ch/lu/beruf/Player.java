package ch.lu.beruf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Player {
  private String name;
  private int health = 3;
  private PlayerScore score = new PlayerScore(this);
  private int playerId;

  public Player(String name, int playerId) {
    this.name = name;
    this.playerId = playerId;
  }

  public static Player loadPlayer() {
    String playerName = "Guest";
    String playerPassword = "guest";
    String path = getLoginSavePath();
    File file = new File(path);
    if (file.exists()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
        playerName = reader.readLine();
        playerPassword = reader.readLine();
      } catch (IOException e) {
        new ExceptionWarning(e);
      }
    }
    Player player;
    if (Main.isConnected() && (player = Main.getQuery().logIn(playerName, playerPassword)) != null) {
      return player;
    } else {
      return new Player("Guest", 1);
    }
  }

  private static String getLoginSavePath() {
    String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    int lastIndexOfWortblitz = path.toLowerCase().lastIndexOf("wortblitz");
    return path.substring(1, lastIndexOfWortblitz) + "loginSave.txt";
  }

  public void savePlayer() {
    String path = getLoginSavePath();
    File file = new File(path);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        new ExceptionWarning(e);
      }
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));) {
      writer.write(getName());
      writer.newLine();
      writer.write(Main.getQuery().getPassword(this));
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  public int getPlayerId() {
    return playerId;
  }

  public void looseHealth() {
    health--;
  }

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public PlayerScore getPlayerScore() {
    return score;
  }

  public boolean isDead() {
    return health == 0;
  }

  public void setFullHealth() {
    health = 3;
  }
}
