package ch.lu.beruf;

public class BossDifficutly {

  private int chanceOfRevealCharOn;
  private int reductionOfTimeBondPerShip;
  private int timeBondTillSpawnNextShip;
  private double speed;

  public BossDifficutly(int chanceOfRevealChar, int reductionOfTimeBondPerShip, int timeBondTillSpawnNextShip,
      double speed) {
    this.chanceOfRevealCharOn = chanceOfRevealChar;
    this.reductionOfTimeBondPerShip = reductionOfTimeBondPerShip;
    this.timeBondTillSpawnNextShip = timeBondTillSpawnNextShip;
    this.speed = speed;

  }

  public int getChanceOfRevealChar() {
    return chanceOfRevealCharOn;
  }

  public double getSpeed() {
    return speed;
  }

  public int getTimeBondTillSpawnNextShip() {
    return timeBondTillSpawnNextShip;
  }

  public int getReductionOfTimeBondPerShip() {
    return reductionOfTimeBondPerShip;
  }
}
