import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Thread;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

/*
 * @brief Responsible for handling all interactions with users
 */
public class UserManagement implements PropertyChangeListener
{
  Map<String, User> users = new HashMap<>();
  private MessageContainer messageContainer;
  ServerMessageHandler serverMessageHandler;
  User lastDeletedUser;
  Logger logger = Logger.getLogger("MDMLogger");
  FileHandler fh;
  private boolean isLoggerSet = false;

  /*
   * @brief Default constructor
   */
  public UserManagement()
  {
  }

  /*
   * @brief Constructor which sets the serverMessageHandler
   * @param msgHandler The server's message handler for interacting with the client
   */
  public UserManagement(ServerMessageHandler msgHandler)
  {
    this.serverMessageHandler = msgHandler;
  }

  /*
   * @brief Sets the messageContainer and performs the requested task
   * @param evt A triggered event that UserManagement is waiting to act on
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    System.out.println("user management activity");
    this.messageContainer = (MessageContainer)evt.getNewValue();
    this.performRequestedTask();
  }

  /*
   * @brief sets up the logger for the report logs
   */
  private void setupLogger()
  {
    boolean isHandlerAdded = false;
    try
    {
      fh = new FileHandler("./logs/AutomaticUserReportLog.log", true);
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    }
    catch (SecurityException e)
    {  
      e.printStackTrace();  
    }
    catch (IOException e)
    {  
      e.printStackTrace();  
    } 
  }

  /*
   * @brief Attempts to execute the requested task, builds the response message, and sends it
   */
  public void performRequestedTask()
  {
    User user;
    List<User> userList = new ArrayList<>();
    StringBuilder returnMsg = new StringBuilder();
    boolean isSuccessful = false;
    boolean isHandled = true;

    this.setupLogger();

    switch(messageContainer.menuOption)
    {
      case ADD_USER:
        user = this.createUser(messageContainer.messageContents);
        isSuccessful = this.addUser(user);
        if (isSuccessful)
        {
          returnMsg.append("Successfully added user!\n");
          break;
        }
        returnMsg.append("Failed to add user: User already exists or invalid data given.\n");
        break;

      case ADD_USERS:
        userList = this.createUserList(messageContainer.messageContents);
        isSuccessful = this.addUsers(userList);
        if (isSuccessful)
        {
          returnMsg.append("Successfully added list of users!\n");
          break;
        }
        returnMsg.append("Failed to add list of users. One of the users likely already exists.\n");
        break;

      case UPDATE_USER:
        user = this.createTempUpdateUser(messageContainer.messageContents);
        isSuccessful = this.updateUser(user);
        if (isSuccessful)
        {
          returnMsg.append("Successfully updated user!\n");
          break;
        }
        returnMsg.append("Failed to update the user. Does not exist\n");
        break;

      case DELETE_USER:
        String userName = messageContainer.messageContents.get(0);
        isSuccessful = this.deleteUser(userName);
        if (isSuccessful)
        {
          returnMsg.append("Successfully deleted user!\n");
          break;
        }
        returnMsg.append("Failed delete user!\n");
        break;

      case LIST_USER_DETAILS:
        user = this.getUser(messageContainer.messageContents.get(0).substring(1));
        returnMsg = new StringBuilder();
        if (user != null)
        {
          isSuccessful = true;
          returnMsg.append(user.fullName + " has " + user.numOfAssociatedAccounts + " associated accounts with the address: " 
            + user.address + " and email: " + user.email);
          break;
        }
        returnMsg.append("user does not exist!\n");
        break;

      case LIST_ALL_USERS:
        isSuccessful = true;
        int i = 1;
        for (String eachUserName : this.users.keySet()) {
          returnMsg.append("user " + i + ": " + eachUserName + " ");
          i++;
        }
        if (i == 1)
        {
          returnMsg.append("no users yet");
        }
        break;

      default:
        isHandled = false;
        System.out.println("Nothing to be done by User Manager.\n");
        break;
    }
    fh.close();
    if (isHandled) {
      serverMessageHandler.buildAndSendResponseMessage(messageContainer.menuOption, isSuccessful, returnMsg.toString());
    }
  }

  /*
   * @brief Creates the specified user based on the messageContents
   * @param messageContents A list of strings with the user's inputted information
   * @return The created user
   */
  public User createUser(List<String> messageContents)
  {
    String fullName = messageContainer.messageContents.get(0) 
                      + " " + messageContainer.messageContents.get(1);
    String phoneNumber = messageContainer.messageContents.get(2);
    String address = messageContainer.messageContents.get(3);
    String email = messageContainer.messageContents.get(4);

    return new User(fullName, address, email);
  }

  public User createTempUpdateUser(List<String> messageContents)
  {
    String fullName = messageContainer.messageContents.get(0);
    String phoneNumber = messageContainer.messageContents.get(1);
    String address = messageContainer.messageContents.get(2);
    String email = messageContainer.messageContents.get(3);

    return new User(fullName, address, email);
  }

  /*
   * @brief Creates the specified users based on the messageContents
   * @param messageContents A list of strings with the users' inputted information
   * @return A list of created users
   */
  public List<User> createUserList(List<String> messageContents) {
    List<User> userList = new ArrayList<>();
    for (int i = 0; i < messageContainer.messageContents.size(); i+=5) {
      String fullName = messageContainer.messageContents.get(i) + " " + messageContainer.messageContents.get(i + 1);
      // String phoneNumber = messageContainer.messageContents.get(i + 2);
      String address = messageContainer.messageContents.get(i + 3);
      String email = messageContainer.messageContents.get(i + 4);
      userList.add(new User(fullName, address, email));
    }

    return userList;
  }

  /*
   * @brief Adds the messageContents' username
   * @param messageContents A list of strings with the users' inputted information
   * @return A list of usernames
   */
  public List<String> createUserNameList(List<String> messageContents)
  {
    //serverMessageHandler.sendMessage("User" + user.getName() + " added to the list of users.\n");
    List<String> userNameList = new ArrayList<>();
    for (String userName : messageContents)
    {
      userNameList.add(userName);
    }

    return userNameList;
  }

  /*
   * @brief Adds the user to storage
   * @param user The specified user
   * @return True or false if it was successful or not
   */
  public boolean addUser(User user)
  {
    if (users.containsKey(user.getName()))
    {
      System.out.println("Cannot add: " + user.getName() + " because they already exist in the system.\n");
      return false;
    }
    users.put(user.fullName, user);
    // System.out.println("User: " + user.getName() + " added to the list of users.\n");
    logger.info("User added! The user's info is: " + user.toString() + "\n");
    return true;
  }

  /*
   * @brief Adds the users to storage
   * @param user The specified users
   * @return True or false if it was successful or not
   */
  public boolean addUsers(List<User> userList)
  {
    for (User user : userList)
    {
      boolean success = addUser(user);
      if (!success)
      {
        System.out.println("Failed to add atleast one user. Returning false.");
        return false;
      }
    }
    return true;
  }

  /*
   * @brief Gets the user tied to the fullName
   * @param fullName The specified user's full name
   * @return The user tied to the full name
   */
  public User getUser(String fullName)
  {
    if (null != users.get(fullName))
    {
      return users.get(fullName);
    }
    System.out.println("Specified user does not exist in the known users list.\n");
    return null;
  }

  /*
   * @brief Update the user with the new information
   * @param user The user information to be changed to
   * @return True or false if it was successful or not
   */
  public boolean updateUser(User user)
  {
    User userUpdate = users.get(user.fullName);
    String tempString = userUpdate.toString();
    if (users.containsKey(user.fullName))
    {
      userUpdate.address = user.address;
      userUpdate.email = user.email;
      logger.info("User updated! The user's info was: " + tempString + " And is now: " + userUpdate.toString() + "\n");
      return true;
    }
    return false;
  }
  
  /*
   * @brief Delete the specified user
   * @param fullName The full name tied to the user
   * @return True or false if it was successful or not
   */
  public boolean deleteUser(String fullName)
  {
    if (!users.containsKey(fullName))
    {
      return false;
    }
    System.out.println("WARNING: Removing user: " + fullName + " and all associated accounts.\n");
    lastDeletedUser = users.get(fullName);
    logger.info("User deleted! The user's info was: " + lastDeletedUser.toString() + " and all associated accounts are to be removed.\n");
    users.remove(fullName);
    return true;
  }

  /*
   * @brief Delete the specified users
   * @param userNameList A list of usernames
   * @return True or false if it was successful or not
   */
  public boolean deleteUsers(List<String> userNameList)
  {
    boolean successfulDelete = false;
    for (String userName : userNameList)
    {
      this.lastDeletedUser = users.get(userName);
      successfulDelete = deleteUser(userName);
      if (successfulDelete == false)
      {
        System.out.println("Could not remove atleast 1 user. Check above output for user removal details.\n");
        break;
      }
    }
    return successfulDelete;
  }

  /*
   * @brief Add the associated account number to the user
   * @param user The user to have an account number added
   */
  public boolean addAssociatedAccountsNo(User user, String phoneNumber) 
  {
    User userNumOfAssociatedAccounts = users.get(user.fullName);
    if (null != userNumOfAssociatedAccounts && null != phoneNumber)
    {
      if (userNumOfAssociatedAccounts.phoneNumbers.contains(phoneNumber))
      {
        System.out.println("Cannot add multiple accounts with same number to user. Nothing added.\n");
        return false;
      }
      userNumOfAssociatedAccounts.numOfAssociatedAccounts += 1;
      userNumOfAssociatedAccounts.phoneNumbers.add(phoneNumber);
      return true;
    }
    System.out.println("Specified user or phone number does not exist.\n");
    return false;
  }

  public boolean removeAssociatedAccountsNo(User user, String phoneNumber)
  {
    User userNumOfAssociatedAccounts = users.get(user.fullName);
    if (null != userNumOfAssociatedAccounts && null != phoneNumber)
    {
      if (!userNumOfAssociatedAccounts.phoneNumbers.contains(phoneNumber))
      {
        System.out.println("Cannot remove account that does not exist with user. Nothing done.\n");
        return false;
      }
      userNumOfAssociatedAccounts.numOfAssociatedAccounts -= 1;
      userNumOfAssociatedAccounts.phoneNumbers.remove(phoneNumber);
      return true;
    }
    System.out.println("Specified user or phone number does not exist.\n");
    return false;
  }

  public User getLastDeletedUser()
  {
    return this.lastDeletedUser;
  }
}
