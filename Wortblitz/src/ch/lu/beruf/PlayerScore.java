package ch.lu.beruf;

public class PlayerScore {
  private int numberOfCharactersTyped = 0;
  private int deletedCharacters = 0;
  private Player player;

  public PlayerScore(Player player) {
    this.player = player;
  }

  private void characterDeleted() {
    deletedCharacters++;
  }

  private void characterTyped() {
    numberOfCharactersTyped++;
  }

  public int getDeletedCharacters() {
    return deletedCharacters;
  }

  public int getNumberOfCharactersTyped() {
    return numberOfCharactersTyped;
  }

  public int getScore() {
    return numberOfCharactersTyped - 5 * deletedCharacters - 20 * (3 - player.getHealth());
  }

  public void resetScore() {
    numberOfCharactersTyped = 0;
    deletedCharacters = 0;
  }

  public void giveChange(String oldValue, String newValue) {
    if (newValue.length() + 1 == oldValue.length() && (newValue.length() != 0 || oldValue.length() == 1)) {
      characterDeleted();
    } else if (newValue.length() != 0) {
      characterTyped();
    }
  }
}
