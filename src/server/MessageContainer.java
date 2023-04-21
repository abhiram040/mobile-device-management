import java.util.ArrayList;
import java.util.List;

/*
 * @brief Responsible for storing the server response message in an easier way to use
 */
public class MessageContainer
  {
    public MenuOption menuOption;
    public List<String> messageContents;
    public boolean isSuccessful;

    /*
     * @brief Default constructor to initialize the messageContents
     */
    public MessageContainer()
    {
      this.messageContents = new ArrayList<String>();
    } 
  }