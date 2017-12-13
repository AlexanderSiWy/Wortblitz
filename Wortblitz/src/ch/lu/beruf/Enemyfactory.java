package ch.lu.beruf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.image.Image;

public class Enemyfactory {

  private ArrayList<String> words = new ArrayList<>();
  private Level level;
  private int createdBossMiniShips = 0;

  public Enemyfactory(String wordsPath, Level level) {
    this.level = level;
    readWordsFile(wordsPath);
  }

  private void readWordsFile(String wordsPath) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(Main.class.getResourceAsStream(wordsPath)));) {
      String word = "";
      while ((word = reader.readLine()) != null) {
        words.add(word);
      }
    } catch (IOException e) {
      new ExceptionWarning(e);
    }
  }

  public Enemy createShip() {
    String text = getRandomWord();
    Image image = setImage(text);
    double speed = getRandomSpeed();
    double x = getRandomX();

    Enemy enemy = new Enemy(text, image, speed, x, level);
    Thread thread = new Thread(enemy);
    thread.setDaemon(true);
    thread.start();
    return enemy;
  }

  public Enemy createBossMiniShip(double x, double y) {
    createdBossMiniShips++;
    String text = getRandomWord();
    Image image = setImage(text);
    double speed = getRandomSpeed();
    int offset = getRandomXOffset();

    Enemy enemy = new Enemy(text, image, speed, x - image.getWidth() / 2, y - image.getHeight(), offset, level);
    Thread thread = new Thread(enemy);
    thread.setDaemon(true);
    thread.start();
    return enemy;
  }

  private String getRandomWord() {
    int random = new Random().nextInt(words.size());
    String text = words.get(random);
    words.remove(random);
    return text;
  }

  private double getRandomX() {
    double x = new Random().nextInt((int) (Main.WIDTH - 150));
    x = x < 150 ? 150 : x;
    return x;
  }

  private double getRandomSpeed() {
    return new Random().nextDouble() + 0.1;
  }

  private int getRandomXOffset() {
    int randomOffset = new Random().nextInt(300);
    randomOffset = randomOffset < 100 ? 100 : randomOffset;
    return createdBossMiniShips % 3 == 0 ? randomOffset : createdBossMiniShips % 2 == 0 ? -randomOffset : 0;
  }

  private Image setImage(String text) {
    Image image;
    if (text.length() > 10) {
      image = new Image(Main.class.getResourceAsStream("images/hardEnemy.png"));
    } else if (text.length() > 5) {
      image = new Image(Main.class.getResourceAsStream("images/normalEnemy.png"));
    } else {
      image = new Image(Main.class.getResourceAsStream("images/easyEnemy.png"));
    }
    return image;
  }
}
