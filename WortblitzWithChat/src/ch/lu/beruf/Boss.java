package ch.lu.beruf;

import javafx.scene.image.Image;

public class Boss extends Enemy {

  private Enemyfactory enemyFactory;
  private int revealdChars = 0;

  private Level level;
  private BossDifficutly bossDifficutly;

  protected Boss(String text, Image image, double speed, String wordsPath, Level level, BossDifficutly bossDifficutly) {
    super(text, image, speed, 130, level);
    this.level = level;
    this.bossDifficutly = bossDifficutly;
    enemyFactory = new Enemyfactory(wordsPath, level);
    this.text.setText(getTextAsString());
  }

  public BossDifficutly getBossDifficutly() {
    return bossDifficutly;
  }

  public String getTextAsString() {
    String text = super.getOriginalText();
    StringBuilder hiddenText = new StringBuilder(text);
    for (int i = revealdChars; i < text.length(); i++) {
      hiddenText.replace(i, i + 1, "X");
    }
    return hiddenText.toString();
  }

  public void revealChar() {
    revealdChars++;
    text.setText(getTextAsString());
  }

  @Override
  public void run() {
    isFlying = true;
    startEnemySpawning();
    while (isFlying) {
      y += speed;
      if (isOnGround()) {
        isFlying = false;
        level.gameOver();
        break;
      }
      reloadPostition();
      try {
        Thread.sleep(25);
      } catch (InterruptedException e) {
        new ExceptionWarning(e);
      }
    }
  }

  private void startEnemySpawning() {
    Thread enemySpawning = new Thread(() -> {
      double xCalculated = x + imageWidth / 2;
      try {
        int bond = bossDifficutly.getTimeBondTillSpawnNextShip();
        while (isFlying && level.isRunning()) {
          level.addShip(enemyFactory.createBossMiniShip(xCalculated, y + imageHeigth));
          Thread.sleep(bond);
          bond -= bossDifficutly.getReductionOfTimeBondPerShip();
        }
      } catch (InterruptedException e) {
        new ExceptionWarning(e);
      }
    });
    enemySpawning.start();
  }

  public boolean isTextRevealed() {
    return revealdChars + 1 >= originalText.length();
  }
}
