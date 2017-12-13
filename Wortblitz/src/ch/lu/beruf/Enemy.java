package ch.lu.beruf;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Enemy implements Runnable {

  private static final int OFFSET = 37;
  private ImageView imageView;
  public final String originalText;
  protected double imageHeigth;
  protected double imageWidth;
  protected Text text;

  protected double speed;

  protected double x;
  protected double y;
  protected int xCorrection = 0;

  private Thread thread;
  protected boolean isFlying = false;
  private Level level;
  private Clip explosionSound;

  protected Enemy(String text, Image image, double speed, double x, Level level) {
    this.speed = speed;
    this.level = level;

    imageHeigth = image.getHeight();
    imageWidth = image.getWidth();
    imageView = new ImageView(image);
    imageView.setFitHeight(imageHeigth);
    imageView.setFitWidth(imageWidth);
    imageView.setX(x);

    this.x = x;
    y = -imageHeigth - OFFSET;

    originalText = text;
    this.text = new Text(text);
    this.text.setFont(Font.loadFont(Main.class.getResourceAsStream("VT323-Regular.ttf"), 30));
    this.text.setX(calculateTextPostition(text));
    reloadPostition();
    AudioInputStream ais;
    try {
      explosionSound = AudioSystem.getClip();
      ais = AudioSystem
          .getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("sound/explosion.wav")));
      explosionSound.open(ais);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      new ExceptionWarning(e);
    }
  }

  protected Enemy(String text, Image image, double speed, double x, double y, int xCorrection, Level level) {
    this(text, image, speed, x, level);
    this.y = y;
    this.xCorrection = xCorrection;
  }

  public boolean isFlying() {
    return isFlying;
  }

  protected double calculateTextPostition(String text) {
    return x + imageWidth / 2 - text.length() * 5;
  }

  protected void reloadPostition() {
    Platform.runLater(() -> {
      imageView.setY(y);
      text.setY(y - 5);
      imageView.setX(x);
      text.setX(calculateTextPostition(originalText));
    });
  }

  protected boolean isOnGround() {
    return y >= (Main.HEIGHT - imageHeigth - OFFSET);
  }

  @Override
  public void run() {
    isFlying = true;
    while (isFlying) {
      moveEnemy();
      if (isOnGround()) {
        isFlying = false;
        level.enemyLanded();
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

  private void moveEnemy() {
    y += speed;
    if (xCorrection != 0) {
      if (xCorrection > 0) {
        x += speed;
        xCorrection--;
      } else {
        x -= speed;
        xCorrection++;
      }
    }
  }

  public void kill() {
    Thread explosion = new Thread(() -> {
      isFlying = false;
      text.setText(null);
      imageView.setImage(new Image(Main.class.getResourceAsStream("images/explosion.gif")));
      explosionSound.start();
      try {
        Thread.sleep(1100);
      } catch (InterruptedException e1) {
        new ExceptionWarning(e1);
      }
      imageView.setImage(null);
      explosionSound.stop();
      explosionSound.close();
    }

    );
    explosion.setDaemon(true);
    explosion.start();
  }

  public Thread getThread() {
    return thread;
  }

  public String getOriginalText() {
    return originalText;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public Text getText() {
    return text;
  }

  public double getSpeed() {
    return speed;
  }

  public void setIsFlying(boolean bool) {
    isFlying = bool;
  }
}
