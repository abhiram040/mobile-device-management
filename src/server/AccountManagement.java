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
    String userId;
    String phoneNumber;
    String bundleName;
    String accountId;
    User user;
    Bundle bundle;
    ServiceAccount account;
    // case wise handling of message passed down by server
    switch(messageContainer.menuOption)
    {
      case ADD_ACCOUNT_V1:
        // Needs fixes for the userID part
        /*userId = messageContainer.messageContents.get(1);
        user = new User(userId);
        phoneNumber = messageContainer.messageContents.get(0);
        bundleName = messageContainer.messageContents.get(2);
        bundle = new Bundle(bundleName);
        this.addServiceAccount(user, phoneNumber, bundle);
        serverMessageHandler.sendMessage("Added Service Account!\n");*/
        break;

      case ADD_ACCOUNT_V2:
        // Need to find a way to add an existing account from accound ID string
        /*accountId = messageContainer.messageContents.get(0);
        this.addServiceAccount(account);
        serverMessageHandler.sendMessage("Added Service Account!\n");*/
        break;

      case DELETE_ACCOUNT:
        phoneNumber = messageContainer.messageContents.get(0);
        this.deleteServiceAccount(phoneNumber);
        break;

      case UPDATE_ACCOUNT:
        phoneNumber = messageContainer.messageContents.get(0);
        bundleName = messageContainer.messageContents.get(1);
        bundle = new Bundle(bundleName);
        this.updateServiceAccount(phoneNumber, bundle);
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
