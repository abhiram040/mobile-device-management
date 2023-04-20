import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class AccountManagement implements PropertyChangeListener
{
  Map<String, ServiceAccount> accounts = new HashMap<>();
  MessageContainer messageContainer;

  ServerMessageHandler serverMessageHandler;

  public AccountManagement()
  {

  }

  public AccountManagement(ServerMessageHandler msgHandler)
  {
    this.serverMessageHandler = msgHandler;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    this.messageContainer = (MessageContainer)evt.getNewValue();
    this.performRequestedTask();
  }

  public void performRequestedTask()
  {
    StringBuilder returnMsg = new StringBuilder();
    // case wise handling of message passed down by server
    switch(messageContainer.menuOption)
    {
      case ADD_ACCOUNT_V1:
        break;

      case ADD_ACCOUNT_V2:
        break;

      case DELETE_ACCOUNT:
        break;

      case UPDATE_ACCOUNT:
        break;

      case ADD_PRE_BUNDLE:
        break;

      case ADD_PAC_BUNDLE_V1:
        break;

      case ADD_PAC_BUNDLE_V2:
        break;

      case ADD_PAC_BUNDLE_V3:
        break;

      case LIST_USER_DETAILS:
        break;

      case LIST_ALL_USERS:
        break;

      case LIST_ACCOUNT:
        break;

      case LIST_ACCOUNTS:
        break;

      case LIST_MONTHLY_FEES:
        break;

      case LIST_MONTHLY_FEES_ALL:
        break;

      case LIST_BUNDLE_DETAILS:
        break;

      case LIST_ALL_PRE_BUNDLES:
        break;

      case LIST_ALL_PAC_BUNDLES:
        break;

      default:
        System.out.println("Nothing to be done by AccountManager.\n");
        break;
    }
  }

  public void addServiceAccount(ServiceAccount account)
  {
    if (null == account)
    {
      throw new IllegalArgumentException("Service account invalid. Nothing added by Account Manager.\n");
    }
    accounts.put(account.phoneNumber, account);
  }

  public void addServiceAccount(User user, String phoneNumber, Bundle bundle)
  {
    if (null == user || null == phoneNumber || null == bundle)
    {
      throw new IllegalArgumentException("Service account details invalid. Nothing added by Account Manager.\n");
    }

    // create account
    ServiceAccount newAccount = new ServiceAccount(phoneNumber, user, bundle);
    accounts.put(phoneNumber, newAccount);
  }

  ServiceAccount getServiceAccount(String phoneNumber)
  {
    return accounts.get(phoneNumber);
  }

  void updateServiceAccount(String phoneNumber, Bundle newBundle)
  {
    ServiceAccount account = accounts.get(phoneNumber);

    if (account != null)
    {
      account.changeBundle(newBundle);
    }
  }

  void deleteServiceAccount(String phoneNumber)
  {
    accounts.remove(phoneNumber);
  }
}
