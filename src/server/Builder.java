import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Builder
{
  private MessageContainer messageContainer;
  private PropertyChangeSupport support;
  private AccountManagement accountManagement;
  private BundleManagement bundleManagement;
  private UserManagement userManagement;

  public Builder()
  {
    this.support = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl)
  {
    this.support.addPropertyChangeListener(pcl);
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl)
  {
    this.support.removePropertyChangeListener(pcl);
  }

  public void sendMessageContainer(MessageContainer messageContainer)
  {
    this.support.firePropertyChange("Client Message", this.messageContainer, messageContainer);
    this.messageContainer = messageContainer;
  }

  public void handleResponse(ServerMessageHandler serverMessageHandler)
  {
    String message = serverMessageHandler.retrieveMessage();
    MessageContainer messageContainer = parseMessage(message);

    if (this.accountManagement == null) {
      this.accountManagement = new AccountManagement(serverMessageHandler);
      this.bundleManagement = new BundleManagement(serverMessageHandler);
      this.userManagement = new UserManagement(serverMessageHandler);
      this.addPropertyChangeListener(accountManagement);
      this.addPropertyChangeListener(bundleManagement);
      this.addPropertyChangeListener(userManagement);
    }

    System.out.println("Message Option: " + messageContainer.menuOption);
    for (String messageContent : messageContainer.messageContents)
    {
      System.out.println("Message Content: " + messageContent);
    }
    handleMessage(messageContainer);
  }

  // rather than doing handling itself, now delegates work to each manager
  public void handleMessage(MessageContainer messageContainer)
  {
    System.out.println("Delegating work for task: " + messageContainer.menuOption + " to different managers.");
    sendMessageContainer(messageContainer);
  }

  // TODO: The messages could be formatted better so the parsing is less error prone
  // Doesn't handle blank entries or entires with ';' currently
  // Likely fine for now, but this area could be improved later if we have time
  private MessageContainer parseMessage(String messageFromClient)
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
