import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
 * @brief Responsible for sending messages to the client, receiving messages
 *        from the client, and parsing them into a usable format
 */
public class ServerMessageHandler
{
  private int port = 8080;
  private ServerSocket server;
  private Socket socket;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;
  List<Socket> clients = new ArrayList<>();

  /*
   * @brief Constructor that creates the connection to the client and creates
   *        the streams for sending/receiving messages
   */
  public ServerMessageHandler(int portNo)
  {
    this.port = portNo;
    try
    {
      server = new ServerSocket(port);

      System.out.println("Waiting for client to connect...");
      socket = server.accept();
      System.out.println("One client has connected! Waiting for messages...");
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
    }
    catch(Exception e)
    {
      System.out.println("Error occurred creating the socket");
    }
  }

  /*
   * @brief Sends a message to the client
   * @param messageToClient A string with the server response information packed
   */
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

  /*
   * @brief Retrieves the next message from the client in a packed format
   * @return The message is a usable format
   */
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
  
  /*
   * @brief Build and send the response message to the client
   * @param menuOption The specified menu option
   * @param isSuccessful True or false based on if the server handled the client signal successfully or not
   * @param message The detailed message to be sent to the client
   */
  public void buildAndSendResponseMessage(MenuOption menuOption, boolean isSuccessful, String message)
  {
    int responseType = isSuccessful ? 1 : 0;
    String messageToClient = menuOption.ordinal() + "=" + responseType + ";" + message;
    
    // TODO: DEBUG - DELETE LATER
    System.out.println("messageToClient: " + messageToClient);
    
    sendMessage(messageToClient);
  }

  /*
   * @brief Parses the message from the client into a usable format
   * @param messageFromClient The message in a packed format
   * @return The message in a usable format
   */
  private MessageContainer parseClientMessage(String messageFromClient)
  {
    System.out.println("message from client: " + messageFromClient);

    MessageContainer messageContainer = new MessageContainer();
    try
    {
      String messageOption = "20";
      for (int i = 0; i < messageFromClient.length(); i++)
      {
        if (messageFromClient.charAt(i) == '=')
        {
          messageOption = messageFromClient.substring(0, i);
          break;
        }
      }
      // TODO: DEBUG - DELETE LATER
      System.out.println("message option is: " + messageOption);
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
      System.out.println("error: " + e);
      System.out.println("Error parsing the message from the client");
    }
    return messageContainer;
  }
}
