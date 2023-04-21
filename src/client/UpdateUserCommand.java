import java.util.List;
import java.util.Arrays;

/*
 * @brief The command which will ask for details specific to UpdateUserCommand
 */
public class UpdateUserCommand extends Command
{
  /*
   * @brief The constructor that sets member variables
   * @param outputText The text prompt to be displayed 
   * @param inputs The initial input to be asked for 
   */
  public UpdateUserCommand(String outputText, List<String> inputs)
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
    String messageToBuild = MenuOption.UPDATE_USER.ordinal() + "=";
    messageToBuild += super.execute();
    List<String> fieldsToUpdate = Arrays.asList("First Name", "Last Name", "Phone Number", "Address", "Email Address");
    for (String fieldToUpdate : fieldsToUpdate)
    {
      System.out.print("Please enter your \'" + fieldToUpdate + "\': ");
      String userInput = System.console().readLine();
      messageToBuild += userInput + ";";
    }
    return messageToBuild;
  }
}
