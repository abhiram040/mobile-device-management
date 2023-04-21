import java.util.List;

/*
 * @brief The command which will ask for details specific to AddPacBundleV1Command
 */
public class AddPacBundleV1Command extends Command
{
  /*
   * @brief The constructor that sets member variables
   * @param outputText The text prompt to be displayed 
   * @param inputs The initial input to be asked for 
   */
  public AddPacBundleV1Command(String outputText, List<String> inputs)
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
    String messageToBuild = MenuOption.ADD_PAC_BUNDLE_V1.ordinal() + "=";
    messageToBuild += super.execute();
    return messageToBuild;
  }
}
