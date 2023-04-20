import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMessageHandler
{
  private final int port = 1234;
  private ServerSocket server;
  private Socket socket;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  public ServerMessageHandler()
  {
    try
    {
      server = new ServerSocket(port);

      System.out.println("Waiting for client to connect...");
      socket = server.accept();
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error occurred creating the socket");
    }
  }

  private void sendMessage(String messageToClient)
  {
    try
    {
      oos.writeObject(messageToClient);
    }
    catch(Exception e)
    {
      System.out.println("Error occurred sending a message");
    }
  }

  public MessageContainer retrieveMessage()
  {
    MessageContainer messageContainer = new MessageContainer();
    try
    {
      String messageFromClient = (String) ois.readObject();
      messageContainer = parseClientMessage(messageFromClient);
    }
    catch(Exception e)
    {
      System.out.println("Error occurred retrieving a message");
    }
    return messageContainer;
  }
  
  public void buildAndSendResponseMessage(MenuOption menuOption, boolean isSuccessful, String message)
  {
    int responseType = isSuccessful ? 1 : 0;
    String messageToClient = menuOption.ordinal() + "=" + responseType + ";" + message;
    
    System.out.println("messageToClient: " + messageToClient);
    
    sendMessage(messageToClient);
  }
  
  // TODO: The messages could be formatted better so the parsing is less error prone
  // Doesn't handle blank entries or entires with ';' currently
  // Likely fine for now, but this area could be improved later if we have time
  private MessageContainer parseClientMessage(String messageFromClient)
  {
    final int MINIMUM_MESSAGE_SIZE = 3;

    MessageContainer messageContainer = new MessageContainer();
    try
    {
      if (messageFromClient.length() < MINIMUM_MESSAGE_SIZE + 1)
      {
        throw new Exception("Invalid message");
      }

      String messageOption = messageFromClient.substring(0,1);
      MenuOption selectedOption = MenuOption.values()[Integer.parseInt(messageOption)];
      messageContainer.menuOption = selectedOption;

      String message = "";
      message = messageFromClient.substring(2, messageFromClient.length());
      if (!message.isEmpty())
      {
        String[] messageContents = message.split(";");
        for (String messageContent : messageContents)
        {
          messageContainer.messageContents.add(messageContent);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("Error parsing the message from the client");
    }
    return messageContainer;
  }
}
