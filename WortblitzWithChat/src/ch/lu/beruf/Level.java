package ch.lu.beruf;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Platform;
import javafx.scene.image.Image;

public class Level implements Runnable {

  private Image backgroundImage;
  private Boss boss;
  private Enemyfactory enemyfactory;
  private LevelGui levelGui;
  private boolean isRunning = false;
  private ArrayList<Enemy> enemys = new ArrayList<>();
  private Player player;
  private int levelID;
  private Difficulty difficulty;
  private Clip backgroundMusic;

  public Level(int levelID, Image backgroundImage, BossData bossData, String wordsPath, Difficulty difficulty) {
    this.levelID = levelID;
    this.backgroundImage = backgroundImage;
    this.difficulty = difficulty;
    this.player = Main.getPlayer();
    enemyfactory = new Enemyfactory(wordsPath, this);
    levelGui = new LevelGui(this);
    boss = new Boss(bossData.getText(), bossData.getImage(), bossData.getSpeed(), bossData.getWordsPath(), this,
        bossData.getBossDifficutly());
  }

  @Override
  public void run() {
    isRunning = true;
    playMusic();
    spawnEnemys();
    spawnBoss();
  }

  private void spawnBoss() {
    if (isRunning) {
      Thread bossThread = new Thread(boss);
      Platform.runLater(() -> levelGui.addShip(boss));
      bossThread.start();
    }
  }

  private void spawnEnemys() {
    int bound = difficulty.getTimeBoundTillSpawnNextShip();
    for (int i = 0; i < difficulty.getNumberOfShips() && isRunning; i++) {
      addShip(enemyfactory.createShip());
      int random = new Random().nextInt(bound);
      random = random > 800 ? random : 800;
      bound -= difficulty.getReductionOfTimeBondPerShip();
      try {
        Thread.sleep(random);
      } catch (InterruptedException e) {
        new ExceptionWarning(e);
      }
    }
  }

  private void playMusic() {
    try {
      backgroundMusic = AudioSystem.getClip();
      AudioInputStream ais;
      ais = AudioSystem
          .getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("sound/backgroundMusic.wav")));
      backgroundMusic.open(ais);
      backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      new ExceptionWarning(e);
    }
  }

  public int getLevelID() {
    return levelID;
  }

  private void killEnemy(Enemy enemy) {
    enemy.kill();
    enemys.remove(enemy);
  }

  public void addShip(Enemy enemy) {
    Platform.runLater(() -> {
      enemys.add(enemy);
      levelGui.addShip(enemy);
    });
  }

  public void checkForKill(String text) {
    if (text.equals(boss.getOriginalText()) && boss.isTextRevealed()) {
      victory();
    }
    for (int i = 0; i < enemys.size(); i++) {
      Enemy enemy = enemys.get(i);
      if (enemy.getOriginalText().equals(text)) {
        levelGui.clearText();
        killEnemy(enemy);
        if (boss.isFlying() && new Random().nextInt(1000) % boss.getBossDifficutly().getChanceOfRevealChar() == 0)
          boss.revealChar();
      }
    }
  }

  private void victory() {
    killAllEnemys();
    boss.kill();
    levelGui.deactivateInput();
    levelGui.showVictoryScreen();
    if (Main.isConnected()) {
      Main.getQuery().safeScore(levelID, player);
    }
  }

  public void enemyLanded() {
    player.looseHealth();
    levelGui.removeHeart();
    if (player.isDead()) {
      gameOver();
    }
  }

  public void gameOver() {
    levelGui.showDeathScreen();
    levelGui.deactivateInput();
    isRunning = false;
  }

  public void killAllEnemys() {
    boss.kill();
    for (Enemy enemy : enemys) {
      enemy.kill();
    }
    enemys.clear();
  }

  public Player getPlayer() {
    return player;
  }

  public ArrayList<Enemy> getEnemys() {
    return enemys;
  }

  public Image getBackgroundImage() {
    return backgroundImage;
  }

  public Boss getBoss() {
    return boss;
  }

  public Enemyfactory getEnemyfactory() {
    return enemyfactory;
  }

  public LevelGui getLevelGui() {
    return levelGui;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setRunning(boolean bool) {
    isRunning = bool;
  }

  public void stopLevel() {
    killAllEnemys();
    backgroundMusic.stop();
    backgroundMusic.close();
    setRunning(false);
  }
}
