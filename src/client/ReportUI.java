import java.util.Map;
import java.util.TreeMap;

/*
 * @brief Responsible for taking user inputs and displaying a prompt and server response
 */
public class ReportUI implements UI
{
  private Map<ReportOption, Command> uiReportMap;

  /*
   * @brief The constructor builds the UI's map
   */
  public ReportUI()
  {
    uiReportMap = createMap();
  }

  /*
   * @brief Displays the UI by iterating through the uiClientMap (or uiReportMap depending on which client calls)
   */
  @Override
  public void printUI()
  {
    System.out.println();
    System.out.println("========== Please Enter A Menu Option ==========");
    for (Map.Entry<ReportOption, Command> mapEntry : uiReportMap.entrySet())
    {
      System.out.print(mapEntry.getKey().ordinal() + 1 + ". ");
      System.out.print(mapEntry.getValue().outputText + " ");

      if (mapEntry.getValue().inputs.size() != 0)
      {
        System.out.print("<");
        for (int i = 0; i < mapEntry.getValue().inputs.size(); ++i)
        {
          System.out.print(mapEntry.getValue().inputs.get(i));
          if (i != mapEntry.getValue().inputs.size() - 1)
          {
            System.out.print(", ");
          }
        }
        System.out.print(">");
      }
      System.out.println();
    }
  }

  /*
   * @brief Displays the response message from the server
   * @param messageContainer The parsed message from the server
   */
  @Override
  public void displayResponse(MessageContainer messageContainer)
  {
    String messageResult = messageContainer.isSuccessful ? "SUCCESS" : "INPUT ERROR";
    
    System.out.println("\n==============================");
    System.out.println("OPERATION: " + messageContainer.menuOption + " = " + messageResult);
    System.out.println("Content: " + messageContainer.messageContents);
    System.out.println("==============================");
  }

  /*
   * @brief Create the UI map by iterating through all of the MenuOption ENUMs
   *        and constructing the Commands to be tied to each of those ENUMs
   * @return A UI map that binds an ENUM option to a command that will build a message to the server
   */
  private Map<ReportOption, Command> createMap()
  {
    Map<ReportOption, Command> newMap = new TreeMap<ReportOption, Command>();
    for (ReportOption menuOption : ReportOption.values())
    {
      Command newCommand = CommandFactory.getInstance().createCommand(menuOption);
      newMap.put(menuOption, newCommand);
    }
    return newMap;
  }

  /*
   * @brief Retrieve the user input from the console and construct a message to send to the server
   * @return The message to the server, constructed based on the user inputs when the related
   *         command is executed
   */
  @Override
  public String retrieveUserInput()
  {
    final int lastMenuVal = ReportOption.values()[ReportOption.values().length - 1].ordinal() + 1;
    System.out.print(" > ");
    String userInput = System.console().readLine();
    String messageToServer = "";

    if (userInput.equalsIgnoreCase("q") || userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("exit"))
    {
      System.exit(0);
    }

    try
    {
      int menuOption = Integer.parseInt(userInput);

      if (menuOption > 0 && menuOption < (lastMenuVal + 1))
      {
        ReportOption selectedOption = ReportOption.values()[menuOption - 1];
        Command commandToExecute = uiReportMap.get(selectedOption);
        messageToServer = commandToExecute.execute();
      }
      else
      {
        throw new Exception("Invalid number");
      }
    }
    catch (Exception e)
    {
      System.out.println("Error: Number expected between 1 and " + lastMenuVal);
    }
    return messageToServer;
  }
}