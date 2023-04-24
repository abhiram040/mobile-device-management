import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/*
 * @brief Responsible for routing the messages to the appropriate service
 */
public class ResponseHandler
{
  private PropertyChangeSupport support;
  private ServerMessageHandler serverMessageHandler;
  private MessageContainer prevClientMessage;

  /*
   * @brief Constructor which sets the message handler and instantiates the PropertyChangeSupport
   * @param serverMessageHandler The server's message handler for interacting with the client
   */
  public ResponseHandler(ServerMessageHandler serverMessageHandler)
  {
    this.support = new PropertyChangeSupport(this);
    this.serverMessageHandler = serverMessageHandler;

    this.prevClientMessage = new MessageContainer();
  }

  /*
   * @brief Binds the property change listener
   * @param pcl The specified property change listener
   */
  public void addPropertyChangeListener(PropertyChangeListener pc)
  {
    this.support.addPropertyChangeListener(pc);
  }

  /*
   * @brief Removes the property change listener
   * @param pcl The specified property change listener
   */
  public void removePropertyChangeListener(PropertyChangeListener pc)
  {
    this.support.removePropertyChangeListener(pc);
  }

  /*
   * @brief Retrieves the message from the client, and dispatches it to the listeners
   */
  public void handleResponse()
  {
    MessageContainer latestClientMessage = serverMessageHandler.retrieveMessage();
    this.support.firePropertyChange("Client Message", prevClientMessage, latestClientMessage);

    prevClientMessage = latestClientMessage;
  }
}

