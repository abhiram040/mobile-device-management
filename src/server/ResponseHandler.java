import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ResponseHandler
{
  private MessageContainer messageContainer;
  private PropertyChangeSupport support;
  private ServerMessageHandler serverMessageHandler;

  public ResponseHandler(ServerMessageHandler serverMessageHandler)
  {
    this.support = new PropertyChangeSupport(this);
    this.serverMessageHandler = serverMessageHandler;
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl)
  {
    this.support.addPropertyChangeListener(pcl);
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl)
  {
    this.support.removePropertyChangeListener(pcl);
  }

  public void handleResponse()
  {
    MessageContainer messageContainer = serverMessageHandler.retrieveMessage();

    // TODO: Debug, can be deleted later
    System.out.println("Message Option: " + messageContainer.menuOption);
    for (String messageContent : messageContainer.messageContents)
    {
      System.out.println("Message Content: " + messageContent);
    }
    handleMessage(messageContainer);
  }

  public void handleMessage(MessageContainer messageContainer)
  {
    System.out.println("Delegating work for task: " + messageContainer.menuOption + " to different managers.");
    sendMessageContainer(messageContainer);
  }

  public void sendMessageContainer(MessageContainer messageContainer)
  {
    this.support.firePropertyChange("Client Message", this.messageContainer, messageContainer);
    this.messageContainer = messageContainer;
  }
}

