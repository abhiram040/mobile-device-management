import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/*
 * @brief Responsible for routing the messages to the appropriate service
 */
public class ResponseHandler
{
  private MessageContainer messageContainer;
  private PropertyChangeSupport support;
  private ServerMessageHandler serverMessageHandler;

  /*
   * @brief Constructor which sets the message handler and instantiates the PropertyChangeSupport
   * @param serverMessageHandler The server's message handler for interacting with the client
   */
  public ResponseHandler(ServerMessageHandler serverMessageHandler)
  {
    this.support = new PropertyChangeSupport(this);
    this.serverMessageHandler = serverMessageHandler;
  }

  /*
   * @brief Binds the property change listener
   * @param pcl The specified property change listener
   */
  public void addPropertyChangeListener(PropertyChangeListener pcl)
  {
    this.support.addPropertyChangeListener(pcl);
  }

  /*
   * @brief Removes the property change listener
   * @param pcl The specified property change listener
   */
  public void removePropertyChangeListener(PropertyChangeListener pcl)
  {
    this.support.removePropertyChangeListener(pcl);
  }

  /*
   * @brief Sends the client's message to the services
   */
  public void handleResponse()
  {
    MessageContainer messageContainer = serverMessageHandler.retrieveMessage();

    // TODO: DEBUG - DELETE LATER
    System.out.println("Message Option: " + messageContainer.menuOption);
    for (String messageContent : messageContainer.messageContents)
    {
      System.out.println("Message Content: " + messageContent);
    }

    this.support.firePropertyChange("Client Message", this.messageContainer, messageContainer);
    this.messageContainer = messageContainer;
  }
}

