import java.util.ArrayList;
import java.util.Arrays;

/*
 * @brief Singleton factory solely responsible for creating Commands
 */
public class CommandFactory
{  
  private static final CommandFactory instance = new CommandFactory();

  /*
   * @brief Default constructor is hidden
   */
  private CommandFactory()
  {
  }

  /*
   * @brief Grab the singleton instance of the CommandFactory
   * @return The singleton instance of the CommandFactory
   */
  public static CommandFactory getInstance()
  {
    return instance;
  }
  
  /*
   * @brief Creates the related command to the inputted MenuOption
   * @param The MenuOption ENUM for the related Command that needs to be built
   * @return The constructed Command based on the inputted MenuOption. If the MenuOption
   *         does not exist, then a StubCommand is constructed
   */
  public Command createCommand(MenuOption menuOption)
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
