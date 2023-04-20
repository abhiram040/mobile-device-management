import java.util.Map;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.ArrayList;

class UI
{
  public final Map<MenuOption, Command> uiMap = createMap();

  public void printUI()
  {
    System.out.println();
    System.out.println("========== Please Enter A Menu Option ==========");
    for (Map.Entry<MenuOption, Command> mapEntry : uiMap.entrySet())
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

  public String retrieveUserInput()
  {
    final int lastMenuVal = MenuOption.values()[MenuOption.values().length - 1].ordinal() + 1;
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
        MenuOption selectedOption = MenuOption.values()[menuOption - 1];
        Command commandToExecute = uiMap.get(selectedOption);
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

  private Map<MenuOption, Command> createMap()
  {
    Map<MenuOption, Command> newMap = new TreeMap<MenuOption, Command>();
    for (MenuOption menuOption : MenuOption.values())
    {
      Command newCommand = buildCommand(menuOption);
      newMap.put(menuOption, newCommand);
    }
    return newMap;
  }
  
  public void displayResponse(MessageContainer messageContainer)
  {
    String messageResult = messageContainer.isSuccessful ? "SUCCESS" : "ERROR";
    
    System.out.println("\n==============================");
    System.out.println("OPERATION: " + messageContainer.menuOption + " = " + messageResult);
    System.out.println("Content: " + messageContainer.messageContents);
    System.out.println("==============================");
  }

  private Command buildCommand(MenuOption menuOption)
  {
    switch(menuOption)
    {
      case ADD_USER:
        return new AddUserCommand("Add User", Arrays.asList("First Name", "Last Name", "Phone Number", "Address", "Email Address"));

      case ADD_USERS:
        return new AddUsersCommand("Add Users", Arrays.asList("First Name", "Last Name", "Phone Number", "Address", "Email Address"));

      case UPDATE_USER:
        return new UpdateUserCommand("Update User", Arrays.asList("Username"));

      case DELETE_USER:
        return new DeleteUserCommand("Delete User", Arrays.asList("Username"));

      case DELETE_USERS:
        return new DeleteUsersCommand("Delete Users", Arrays.asList("Username"));

      case ADD_ACCOUNT_V1:
        return new AddAccountV1Command("Add Account", Arrays.asList("Phone Number", "Username", "Service Bundle"));

      case ADD_ACCOUNT_V2:
        return new AddAccountV2Command("Add Account", Arrays.asList("Account ID"));

      case DELETE_ACCOUNT:
        return new DeleteAccountCommand("Delete Account", Arrays.asList("Phone Number"));

      case UPDATE_ACCOUNT:
        return new UpdateAccountCommand("Update Account", Arrays.asList("Phone Number", "Bundle Name"));

      case ADD_PRE_BUNDLE:
        return new AddPreBundleCommand("Add Preconfigured Bundle", Arrays.asList("Bundle Name"));

      case ADD_PAC_BUNDLE_V1:
        return new AddPacBundleV1Command("Add PaC Bundle", Arrays.asList("Bundle Name"));

      case ADD_PAC_BUNDLE_V2:
        return new AddPacBundleV2Command("Add PaC Bundle with Calling Option", Arrays.asList("Calling Plan Name"));

      case ADD_PAC_BUNDLE_V3:
        return new AddPacBundleV3Command("Add PaC Bundle with Messaging Option", Arrays.asList("Messaging Plan Name"));

      case LIST_USER_DETAILS:
        return new ListUserDetailsCommand("List User Details", Arrays.asList("Username"));

      case LIST_ALL_USERS:
        return new ListAllUsersCommand("List All Users' Names", new ArrayList<String>());

      case LIST_ACCOUNT:
        return new ListAccountCommand("List Account", Arrays.asList("Phone Number"));

      case LIST_ACCOUNTS:
        return new ListAccountsCommand("List Accounts", Arrays.asList("Username"));

      case LIST_MONTHLY_FEES:
        return new ListMonthlyFeesCommand("List Monthly Fees", Arrays.asList("Phone Number"));

      case LIST_MONTHLY_FEES_ALL:
        return new ListMonthlyFeesAllCommand("List Monthly Fees All Accounts", Arrays.asList("Username"));

      case LIST_BUNDLE_DETAILS:
        return new ListBundleDetailsCommand("List Bundle Details", Arrays.asList("Bundle Name"));

      case LIST_ALL_PRE_BUNDLES:
        return new ListAllPreBundlesCommand("List All Preconfigured Bundle Names", new ArrayList<String>());

      case LIST_ALL_PAC_BUNDLES:
        return new ListAllPacBundlesCommand("List All PaC Bundle Names", new ArrayList<String>());

      default:
        System.out.println("Invalid menuOption");
        return new StubCommand();
    }
  }
}
