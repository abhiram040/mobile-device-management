import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class AccountManagement implements PropertyChangeListener
{
  Map<String, ServiceAccount> accounts = new HashMap<>();
  MessageContainer messageContainer;
  UserManagement userManagement;
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
    String fullName;
    String phoneNumber;
    String bundleName;
    String accountId;
    User user;
    Bundle bundle;
    ServiceAccount account;
    boolean isSuccessful = false;
    // case wise handling of message passed down by server
    switch(messageContainer.menuOption)
    {
      case ADD_ACCOUNT_V1:
        phoneNumber = messageContainer.messageContents.get(0);
        fullName = messageContainer.messageContents.get(1);
        user = userManagement.getUser(fullName);
        bundleName = messageContainer.messageContents.get(2);
        bundle = new Bundle(bundleName);
        isSuccessful = this.addServiceAccount(user, phoneNumber, bundle);
        userManagement.addAssociatedAccountsNo(user);
         if (isSuccessful)
        {
          returnMsg.append("Successfully deleted service account!\n");
          break;
        }
        returnMsg.append("Failed to delete service account: Account phone number not associated to a recognized account.\n");
        break;

      case ADD_ACCOUNT_V2:
        // Need to find a way to add an existing account from accound ID string
        /*accountId = messageContainer.messageContents.get(0);
        try {
          account = Integer.parseInt(accountId);
        }
        catch (NumberFormatException e) {
          System.out.println("Account ID cannot be a non-integer value.\n");
        }
        this.addServiceAccount(account);*/
        break;

      case DELETE_ACCOUNT:
        phoneNumber = messageContainer.messageContents.get(0);
        isSuccessful = this.deleteServiceAccount(phoneNumber);
        if (isSuccessful)
        {
          returnMsg.append("Successfully deleted service account!\n");
          break;
        }
        returnMsg.append("Failed to delete service account: Account phone number not associated to a recognized account.\n");
        break;

      case UPDATE_ACCOUNT:
        phoneNumber = messageContainer.messageContents.get(0);
        bundleName = messageContainer.messageContents.get(1);
        bundle = new Bundle(bundleName);
        isSuccessful = this.updateServiceAccount(phoneNumber, bundle);
        if (isSuccessful)
        {
          returnMsg.append("Successfully updated service account!\n");
          break;
        }
        returnMsg.append("Failed to update service account: Phone number does not correspond to known account.\n");
        break;

      default:
        System.out.println("Nothing to be done by AccountManager.\n");
        break;
    }
    serverMessageHandler.buildAndSendResponseMessage(messageContainer.menuOption, isSuccessful, returnMsg.toString());
  }

  public boolean addServiceAccount(ServiceAccount account)
  {
    if (null == account)
    {
      throw new IllegalArgumentException("Service account invalid. Nothing added by Account Manager.\n");
    }
    if (null != accounts.get(account.phoneNumber))
    {
      return false;
    }
    accounts.put(account.phoneNumber, account);
    return true;
  }

  public boolean addServiceAccount(User user, String phoneNumber, Bundle bundle)
  {
    if (null == user || null == phoneNumber || null == bundle)
    {
      throw new IllegalArgumentException("Service account details invalid. Nothing added by Account Manager.\n");
    }

    // create account
    ServiceAccount newAccount = new ServiceAccount(phoneNumber, user, bundle);
    if (null != accounts.get(phoneNumber))
    {
      return false;
    }
    accounts.put(phoneNumber, newAccount);
    return true;
  }

  public ServiceAccount getServiceAccount(String phoneNumber)
  {
    return accounts.get(phoneNumber);
  }

  public boolean updateServiceAccount(String phoneNumber, Bundle newBundle)
  {
    ServiceAccount account = accounts.get(phoneNumber);

    if (account != null)
    {
      account.changeBundle(newBundle);
      return true;
    }
    return false;
  }

  public boolean deleteServiceAccount(String phoneNumber)
  {
    if (null == phoneNumber || null == accounts.get(phoneNumber))
    {
      return false;
    }
    accounts.remove(phoneNumber);
    return true;
  }
}
