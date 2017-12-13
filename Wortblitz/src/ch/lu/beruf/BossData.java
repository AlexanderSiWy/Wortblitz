package ch.lu.beruf;

import javafx.scene.image.Image;

public class BossData {
  private String text;
  private Image image;
  private double speed;
  private String wordsPath;
  private BossDifficutly bossDifficutly;

  public BossData(String text, Image image, String wordsPath, BossDifficutly bossDifficutly) {
    this.text = text;
    this.image = image;
    this.speed = bossDifficutly.getSpeed();
    this.wordsPath = wordsPath;
    this.bossDifficutly = bossDifficutly;
  }

  public String getText() {
    return text;
  }

  public Image getImage() {
    return image;
  }

  public double getSpeed() {
    return speed;
  }

  public String getWordsPath() {
    return wordsPath;
  }

  public BossDifficutly getBossDifficutly() {
    return bossDifficutly;
  }
}
