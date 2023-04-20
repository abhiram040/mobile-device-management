import java.util.List;
import java.util.Arrays;

public class UpdateUserCommand extends Command
{
  public UpdateUserCommand(String outputText, List<String> inputs)
  {
    super(outputText, inputs);
  }

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
