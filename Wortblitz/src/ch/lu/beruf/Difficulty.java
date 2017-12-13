package ch.lu.beruf;

public class Difficulty {
  private int numberOfShips;
  private int timeBoundTillSpawnNextShip;

  private int reductionOfTimeBondPerShip;

  public Difficulty(int numberOfShips, int timeBoundTillSpawnNextShip, int reductionOfTimeBondPerShip) {
    this.numberOfShips = numberOfShips;
    this.timeBoundTillSpawnNextShip = timeBoundTillSpawnNextShip;
    this.reductionOfTimeBondPerShip = reductionOfTimeBondPerShip;

  }

  public int getNumberOfShips() {
    return numberOfShips;
  }

  public int getTimeBoundTillSpawnNextShip() {
    return timeBoundTillSpawnNextShip;
  }

  public int getReductionOfTimeBondPerShip() {
    return reductionOfTimeBondPerShip;
  }

}
