import java.util.Map;
import java.util.TreeMap;

/*
 * @brief Responsible for taking user inputs and displaying a prompt and server response
 */
public interface UI
{
  /*
   * @brief Displays the UI by iterating through the uiClientMap (or uiReportMap depending on which client calls)
   */
  public void printUI();

  /*
   * @brief Displays the response message from the server
   * @param messageContainer The parsed message from the server
   */
  public void displayResponse(MessageContainer messageContainer);

  /*
   * @brief Retrieve the user input from the console and construct a message to send to the server
   * @return The message to the server, constructed based on the user inputs when the related
   *         command is executed
   */
  public String retrieveUserInput();
}
