import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * @brief Responsible for handling all interactions with accounts
 */
public class AccountManagement implements PropertyChangeListener
{
  Map<String, ServiceAccount> accounts = new HashMap<>();
  MessageContainer messageContainer;
  ServerMessageHandler serverMessageHandler;
  ArrayList<ServiceAccount> accountList = new ArrayList<ServiceAccount>();
  ArrayList<ServiceAccount> associatedAccountList = new ArrayList<ServiceAccount>();

  // this class is currently acting as the primary manager to avoid
  // any circular dependancies. Will likely look into replacing referencing
  // with a communication interface
  UserManagement userManagement;
  BundleManagement bundleManagement;
  
  /*
   * @brief Default constructor
   */
  public AccountManagement()
  {
  }

  /*
   * @brief Constructor which sets the serverMessageHandler
   * @param msgHandler The server's message handler for interacting with the client
   */
  public AccountManagement(ServerMessageHandler msgHandler)
  {
    this.serverMessageHandler = msgHandler;
  }

  /*
   * @brief Sets the messageContainer and performs the requested task
   * @param evt A triggered event that AccountManagement is waiting to act on
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    this.messageContainer = (MessageContainer)evt.getNewValue();
    this.performRequestedTask();
  }

  /*
   * @brief Attempts to execute the requested task, builds the response message, and sends it
   */
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
    boolean isHandled = true;

    switch(messageContainer.menuOption)
    {
      case ADD_ACCOUNT_V1:
        phoneNumber = messageContainer.messageContents.get(0);
        fullName = messageContainer.messageContents.get(1);
        user = userManagement.getUser(fullName);
        bundleName = messageContainer.messageContents.get(2);
        bundle = getBundle(bundleName);
        isSuccessful = this.addServiceAccount(user, phoneNumber, bundle);
        if (isSuccessful && userManagement.addAssociatedAccountsNo(user, phoneNumber))
        {
          returnMsg.append("Successfully added service account!\n");
          break;
        }
        returnMsg.append("Failed to add service account: Account phone number not associated to a recognized account, or phone number is already registered with this user.\n");
        break;

      case ADD_ACCOUNT_V2:
        account = this.getServiceAccount(messageContainer.messageContents.get(0));
        isSuccessful = this.addServiceAccount(account);
        if (isSuccessful)
        {
          returnMsg.append("Successfully added service account!\n");
          break;
        }
        returnMsg.append("Failed to add service account: Account phone number not associated to a recognized account.\n");
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
        bundle = getBundle(bundleName);
        isSuccessful = this.updateServiceAccount(phoneNumber, bundle);
        if (isSuccessful)
        {
          returnMsg.append("Successfully updated service account!\n");
          break;
        }
        returnMsg.append("Failed to update service account: Phone number does not correspond to known account.\n");
        break;

      case LIST_ACCOUNT:
        account = this.getServiceAccount(messageContainer.messageContents.get(0).substring(1));
        if (null != account)
        {
          returnMsg.append("Account inforamtion for provided phone number is as below:");
          returnMsg.append("User with name " + account.user.fullName + " with phone number:" + account.phoneNumber + " has subscribed for bundle:" + account.bundle.name);
          isSuccessful = true;
          break;
        }
        returnMsg.append("Cannot list an invalid accounts information.\n");
        isSuccessful = false;
        break;

      case LIST_ACCOUNTS:
        user = userManagement.getUser(messageContainer.messageContents.get(0).substring(1));
        associatedAccountList = getAssociatedAccountsList(user);
        if (null == associatedAccountList || associatedAccountList.size() == 0)
        {
          isSuccessful = false;
          System.out.println("Cannot pull account list to display for that user.\n");
          returnMsg.append("Cannot pull account list to display for that user.");
          break;
        }
        returnMsg.append("List of accounts associated with " + user.fullName + " are listed below: \n");
        for(ServiceAccount acc : associatedAccountList)
        {
          returnMsg.append("Phone Number: " + acc.phoneNumber + " on a " + acc.bundle.name + ". ");
        }
        isSuccessful = true;

        break;

      case LIST_MONTHLY_FEES:
        account = this.getServiceAccount(messageContainer.messageContents.get(0).substring(1));
        if (null != account)
        {
          isSuccessful = true;
          returnMsg.append("User with name " + account.user.fullName + " has monthly fees of $" + account.bundle.monthlyFees + " for the account with phone number " + account.phoneNumber + ".");
          break;
        }
        isSuccessful = false;
        returnMsg.append("User with name " + account.user.fullName + " cannot be found.\n");
        break;

      case LIST_MONTHLY_FEES_ALL:
        user = userManagement.getUser(messageContainer.messageContents.get(0).substring(1));
        if (null == user)
        {
          returnMsg.append("User does not exist in the system. Cannot pull monthly fees.\n");
          isSuccessful = false;
          break;
        }
        associatedAccountList = getAssociatedAccountsList(user);
        double owingSum = 0.0;
        for(ServiceAccount acc : associatedAccountList)
        {
          owingSum += acc.bundle.monthlyFees;
        }
        isSuccessful = true;
        returnMsg.append("User with name " + user.fullName + " has total monthly fees of $" + owingSum + ".");
        break;

      case DELETE_USER:
        isHandled = false;
        user = userManagement.getLastDeletedUser();
        if (null == user)
        {
          System.out.println("Specified user does not exist in the system. Nothing to be done.");
          isSuccessful = false;
          break;
        }
        associatedAccountList = getAssociatedAccountsList(user);
        for(ServiceAccount acc : associatedAccountList)
        {
          this.deleteServiceAccount(acc.phoneNumber);
        }
        isSuccessful = true;
        System.out.println("All associated accounts with " + user.fullName + " have been deleted");
        break;

      default:
        isHandled = false;
        System.out.println("Nothing to be done by Account Manager.\n");
        break;
    }
    if (isHandled) {
      serverMessageHandler.buildAndSendResponseMessage(messageContainer.menuOption, isSuccessful, returnMsg.toString());
    }
  }

  /*
   * @brief Attempts to add the specified service account to storage
   * @param account The specified account
   * @return True or false based on if the operation was successful or not
   */
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

  /*
   * @brief Attempts to add the specified service account to storage
   * @param user The specified user
   * @param phoneNumber The specified phoneNumber
   * @param bundle The specified bundle
   * @return True or false based on if the operation was successful or not
   */
  public boolean addServiceAccount(User user, String phoneNumber, Bundle bundle)
  {
    if (null == user || null == phoneNumber || null == bundle)
    {
      System.out.println("Service account details invalid. Nothing added by Account Manager.\n");
    }

    ServiceAccount newAccount = new ServiceAccount(phoneNumber, user, bundle);
    if (null != accounts.get(phoneNumber))
    {
      return false;
    }
    accounts.put(phoneNumber, newAccount);
    return true;
  }

  /*
   * @brief Attempts to get the specified service account from storage
   * @param phoneNumber The specified phoneNumber
   * @return The related account or null if not found
   */
  public ServiceAccount getServiceAccount(String phoneNumber)
  {
    System.out.println("DEBUG: Looking for phone number: " + phoneNumber);
    if (!accounts.containsKey(phoneNumber))
    {
      System.out.println("Cannot find account with that phone number.\n");
      return null;
    }
    return accounts.get(phoneNumber);
  }
 
  /*
   * @brief Attempts to update the specified service account from storage
   * @param phoneNumber The specified phoneNumber
   * @param newBundle The bundle to be changed to
   * @return True or false based on if the operation was successful or not
   */
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

  /*
   * @brief Attempts to delete the specified service account from storage
   * @param phoneNumber The specified phoneNumber
   * @return True or false based on if the operation was successful or not
   */
  public boolean deleteServiceAccount(String phoneNumber)
  {
    boolean success = false;
    if (null == phoneNumber || null == accounts.get(phoneNumber))
    {
      return false;
    }
    success = this.userManagement.removeAssociatedAccountsNo(accounts.get(phoneNumber).user, phoneNumber);
    accounts.remove(phoneNumber);
    return success;
  }

  /*
   * @brief Attempts to get all of the associated accounts for the specified user from storage
   * @param user The specified user
   * @return All of the accounts linked to the specified user
   */
  public ArrayList<ServiceAccount> getAssociatedAccountsList(User user)
  {
    if (null != user)
    {
      ArrayList<ServiceAccount> accountList = new ArrayList<ServiceAccount>();
      for (ServiceAccount account : this.accounts.values()) 
      {
        if (account.user.fullName == user.fullName)
        {
          accountList.add(account);
        }
      }
      return accountList;
    }
    return null;
  }

  /*
   * @brief Sets the management services
   * @param bm The bundle management service
   * @param um The user manamagement service
   */
  public void setMangerReferences(BundleManagement bm, UserManagement um)
  {
    this.bundleManagement = bm;
    this.userManagement = um;
  }

  /*
   * @brief Gets the bundle tied to the associated bundle name
   * @param bundleName The specified bundle name
   * @return The bundle associated with the bundle name
   */
  public Bundle getBundle(String bundleName)
  {
    Bundle bundle;
    if (!bundleManagement.isBundleRegistered(bundleName))
        {
          bundle = new Bundle(bundleName);
          bundleManagement.addBundle(bundle);
          return bundle;
        }
        else
        {
          bundle = bundleManagement.getBundle(bundleName);
          return bundle;
        }
  }
}
