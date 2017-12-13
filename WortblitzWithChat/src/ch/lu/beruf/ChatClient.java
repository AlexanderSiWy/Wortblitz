package ch.lu.beruf;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ChatClient {

  private Socket socket;
  private boolean isInChat = true;

  public ChatClient(TextArea chatHistory, Label userCurrentlyInChat) throws IOException {
    try {
      socket = new Socket("wyssddns.internet-box.ch", 1231);
      try {
        InputStream inputStream = socket.getInputStream();
        byte[] byteArray = new byte[1024];
        Thread chatInputThread = new Thread(() -> {
          int length;
          try {
            while (isInChat() && (length = inputStream.read(byteArray)) > -1) {
              String input = "";
              for (int i = 0; i < length; i++) {
                input = input.concat(String.valueOf((char) byteArray[i]));
              }
              if (!input.contains(":")) {
                int lastIndex = input.lastIndexOf("!");
                int playerNumberInChat = Integer.parseInt(input.substring(lastIndex + 1));
                Platform.runLater(() -> userCurrentlyInChat.setText("Users: " + playerNumberInChat));
                input = input.substring(0, lastIndex + 1) + "\n";
              }
              chatHistory.insertText(chatHistory.getText().length(), input);
            }
          } catch (IOException e) {
            new ExceptionWarning(e);
          }
        });
        chatInputThread.setDaemon(true);
        chatInputThread.start();
      } catch (IOException e) {
        new ExceptionWarning(e);
      }
    } catch (IOException e) {
      new ExceptionWarning(e);
    }
  }

  private void sendExitMessage() {
    sendMessage(">>exit");
  }

  public void setPlayer() {
    sendMessage(">>player:" + Main.getPlayer().getName());
  }

  public void sendMessage(String text) {
    try {
      socket.getOutputStream().write(text.getBytes());
    } catch (IOException e) {
      new ExceptionWarning(e);
    }
  }

  public boolean isInChat() {
    return isInChat;
  }

  public void setInChat(boolean isInChat) {
    this.isInChat = isInChat;
  }

  public void exit() {
    isInChat = false;
    sendExitMessage();
  }
}
