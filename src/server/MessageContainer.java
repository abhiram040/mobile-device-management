import java.util.ArrayList;
import java.util.List;

public class MessageContainer
  {
    public MenuOption menuOption;
    public List<String> messageContents;
    public boolean isSuccessful;

    public MessageContainer()
    {
      this.messageContents = new ArrayList<String>();
    } 
  }