package ch.lu.beruf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Query {

  private static final String URL = "jdbc:mysql://wyssddns.internet-box.ch:3306/wortblitz?user=wortblitz&password=wortblitz";
  private Connection connection;
  private Statement statement;

  public boolean connect() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(URL);
      statement = connection.createStatement();
    } catch (SQLException | ClassNotFoundException e) {
      Alert noConnectionInfo = new Alert(AlertType.WARNING);
      noConnectionInfo.setTitle("No Connection to Database");
      noConnectionInfo.setHeaderText("Sie haben keine Verbindung zur Datenbank!");
      noConnectionInfo.setContentText("Keine Scores werden gespeichert und Sie können sich nicht einloggen.");
      noConnectionInfo.showAndWait();
      return false;
    }
    return true;
  }

  public void safeScore(int levelID, Player player) {
    try {
      PlayerScore playerScore = player.getPlayerScore();
      statement.executeUpdate("insert into score (charsTyped, charsDeleted,score, fk_player, fk_level) values ("
          + playerScore.getNumberOfCharactersTyped() + "," + playerScore.getDeletedCharacters() + ","
          + playerScore.getScore() + "," + +player.getPlayerId() + "," + levelID + ");");
    } catch (SQLException e) {
      new ExceptionWarning(e);
    }
  }

  public String getHighscore(int levelID, Player player) {
    String highscore = "";
    try (ResultSet rs = statement.executeQuery("select score from score where fk_level = " + levelID
        + " and fk_player = " + player.getPlayerId() + " order by score desc;");) {
      if (rs.next()) {
        highscore = rs.getString(1);
      }
    } catch (SQLException e) {
      new ExceptionWarning(e);
    }
    return highscore;
  }

  public String getRank(int levelID, String highscore) {
    String rank = "";
    if (!"".equals(highscore)) {
      try (ResultSet rs = statement.executeQuery(
          "Select count(*) from score where score > " + highscore + " and fk_level = '" + levelID + "';");) {
        if (rs.next()) {
          rank = String.valueOf(rs.getInt(1) + 1);
        }
      } catch (SQLException e) {
        new ExceptionWarning(e);
      }
    }
    return rank;
  }

  public String[] getBestPlayerAndScore(int levelID) {
    String[] bestPlayerAndScore = new String[2];
    try (ResultSet rs = statement.executeQuery(
        "select player.name, score from score inner join player on player.playerid = fk_player where fk_level = "
            + levelID + " order by score desc;");) {
      if (rs.next()) {
        bestPlayerAndScore[0] = rs.getString(1);
        bestPlayerAndScore[1] = rs.getString(2);
      }
    } catch (SQLException e) {
      new ExceptionWarning(e);
    }
    return bestPlayerAndScore;
  }

  public Player logIn(String userName, String password) {
    boolean succsesfull = false;
    try (ResultSet rs = statement.executeQuery(
        "Select count(*), playerId from player where name = '" + userName + "' and password = '" + password + "';");) {
      if (rs.next()) {
        succsesfull = rs.getInt(1) == 1;
      }
      if (succsesfull) {
        return new Player(userName, rs.getInt(2));
      }
    } catch (SQLException e) {
      new ExceptionWarning(e);
    }
    return null;
  }

  public Player register(String userName, String password) {
    boolean usernameNotUsed = false;
    try (ResultSet rs = statement.executeQuery("Select count(*) from player where name = '" + userName + "';");) {
      if (rs.next()) {
        usernameNotUsed = rs.getInt(1) == 0;
      }
      if (usernameNotUsed) {
        statement.executeUpdate("insert into player (name, password) values('" + userName + "','" + password + "');");
        int id = getPlayerId(userName);
        return new Player(userName, id);
      }
    } catch (SQLException e) {
      new ExceptionWarning(e);
    }
    return null;
  }

  private int getPlayerId(String userName) throws SQLException {
    int id = 0;
    try (ResultSet rsPlayerID = statement
        .executeQuery("Select playerId from player where name = '" + userName + "';");) {
      if (rsPlayerID.next()) {
        id = rsPlayerID.getInt(1);
      }
    }
    return id;
  }

  public void closeConnection() {
    try {
      statement.close();
      connection.close();
    } catch (SQLException e) {
      new ExceptionWarning(e);
    }
  }

  public String getPassword(Player player) {
    String password = "";
    try (ResultSet rs = statement
        .executeQuery("Select password from player where playerid = " + player.getPlayerId() + ";");) {

      if (rs.next()) {
        password = rs.getString(1);
      }
    } catch (SQLException e) {
      new ExceptionWarning(e);
    }
    return password;
  }

}
