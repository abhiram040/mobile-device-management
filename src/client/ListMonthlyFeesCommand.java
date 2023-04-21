import java.util.List;

/*
 * @brief The command which will ask for details specific to ListMonthlyFeesCommand
 */
public class ListMonthlyFeesCommand extends Command
{
  /*
   * @brief The constructor that sets member variables
   * @param outputText The text prompt to be displayed 
   * @param inputs The initial input to be asked for 
   */
  public ListMonthlyFeesCommand(String outputText, List<String> inputs)
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
    String messageToBuild = MenuOption.LIST_MONTHLY_FEES.ordinal() + "=";
    messageToBuild += super.execute();
    return messageToBuild;
  }
}
