import java.util.List;

/*
 * @brief The base command which will ask for the initial inputs
 */
public class Command
{
  public String outputText;
  public List<String> inputs;

  /*
   * @brief The default constructor needed by the stub command
   */ 
  public Command()
  {
  }

  /*
   * @brief The constructor that sets member variables
   * @param outputText The text prompt to be displayed 
   * @param inputs The initial input to be asked for 
   */ 
  public Command(String outputText, List<String> inputs)
  {
    this.outputText = outputText;
    this.inputs = inputs;
  }

  /*
   * @brief The execute command which will ask for user input and build
   *        the related message to send to the server
   * @return The built message to send to the server
   */
  public String execute()
  {
    String messageToBuild = "";
    for (String input : inputs)
    {
      System.out.print("Please enter your \'" + input + "\': ");
      String userInput = System.console().readLine();
      messageToBuild += userInput + ";";
    }
    return messageToBuild;
  }
}
