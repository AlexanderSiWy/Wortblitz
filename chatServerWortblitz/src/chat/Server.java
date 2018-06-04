package chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
  static boolean run = true;
  private static ArrayList<Socket> sockets;

  public static void main(String[] args) throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(1231)) {
      sockets = new ArrayList<>();
      while (true) {
        Socket socket = serverSocket.accept();
        sockets.add(socket);
        activate(socket);
      }
    }
  }

  private static void activate(Socket socket) {
    Thread thread = new Thread(() -> {
      InputStream inputStream;
      String playerName = "";
      try {
        inputStream = socket.getInputStream();
        byte[] byteArray = new byte[1024];
        int length;
        while (!socket.isClosed() && (length = inputStream.read(byteArray)) > -1) {
          String input = new String(byteArray, 0, length);
          String setPlayer = ">>player:";
          if (input.startsWith(setPlayer)) {
            System.out.println("received");
            if (!playerName.isEmpty()) {
              playerLeftMessage(socket, playerName);
            }
            playerName = input.substring(setPlayer.length());
            playerJoined(playerName);
          } else if (">>exit".equals(input)) {
            playerLeft(socket, playerName);
          } else {
            String output = playerName + ": " + input + "\n";
            sendToClients(output.getBytes(), output.length());
          }
        }
      } catch (IOException e) {
        playerLeft(socket, playerName);
        e.printStackTrace();
      }
    });
    thread.setDaemon(true);
    thread.start();

  }

  private static void playerLeft(Socket socket, String playerName) {
    try {
      socket.close();
      sockets.remove(socket);
      playerLeftMessage(socket, playerName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void playerJoined(String playerName) throws IOException {
    String playerJoined = playerName + " joined!" + sockets.size();
    sendToClients(playerJoined.getBytes(), playerJoined.length());
  }

  private static void playerLeftMessage(Socket socket, String playerName) {
    try {
      String playerLeft = playerName + " left!" + sockets.size();
      sendToClients(playerLeft.getBytes(), playerLeft.length());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void sendToClients(byte[] byteArray, int length) throws IOException {
    for (Socket socket : sockets) {
      socket.getOutputStream().write(byteArray, 0, length);
    }
  }
}
