import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ResponseHandler
{
  private MessageContainer messageContainer;
  private PropertyChangeSupport support;
  private AccountManagement accountManagement;
  private BundleManagement bundleManagement;
  private UserManagement userManagement;
  private ServerMessageHandler serverMessageHandler;

  public ResponseHandler(ServerMessageHandler serverMessageHandler)
  {
    this.support = new PropertyChangeSupport(this);
    this.serverMessageHandler = serverMessageHandler;
	
    this.accountManagement = new AccountManagement(serverMessageHandler);
    this.bundleManagement = new BundleManagement(serverMessageHandler);
    this.userManagement = new UserManagement(serverMessageHandler);
    
    this.addPropertyChangeListener(accountManagement);
    this.addPropertyChangeListener(bundleManagement);
    this.addPropertyChangeListener(userManagement);
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

  public void handleResponse()
  {
    MessageContainer messageContainer = serverMessageHandler.retrieveMessage();

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
}
