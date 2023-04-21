import java.util.List;

/*
 * @brief The command which will ask for details specific to DeleteUsersCommand
 */
public class DeleteUsersCommand extends Command
{
  /*
   * @brief The constructor that sets member variables
   * @param outputText The text prompt to be displayed 
   * @param inputs The initial input to be asked for 
   */
  public DeleteUsersCommand(String outputText, List<String> inputs)
  {
    super(outputText, inputs);
  }

  /*
   * @brief The execute command which will ask for user input and build
   *        the related message to send to the server
   * @return The built message to send to the server
   */
  @Override
  public String execute()
  {
    String messageToBuild = MenuOption.DELETE_USERS.ordinal() + "=";
    String userInput = "";
    do
    {
      messageToBuild += super.execute();
      System.out.print("Do you want to enter another user? [y/n] ");
      userInput = System.console().readLine();
    }
    while (userInput.equalsIgnoreCase("y"));
    return messageToBuild;
  }
}
