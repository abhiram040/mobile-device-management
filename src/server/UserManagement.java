import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @brief Responsible for handling all interactions with users
 */
public class UserManagement implements PropertyChangeListener
{
  Map<String, User> users = new HashMap<>();
  private MessageContainer messageContainer;
  ServerMessageHandler serverMessageHandler;

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
   * @brief Attempts to execute the requested task, builds the response message, and sends it
   */
  public void performRequestedTask()
  {
    User user;
    List<User> userList = new ArrayList<>();
    StringBuilder returnMsg = new StringBuilder();
    boolean isSuccessful = false;
    boolean isHandled = true;
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
        user = this.createUser(messageContainer.messageContents);
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
          returnMsg.append("Successfully delete user!\n");
          break;
        }
        returnMsg.append("Failed delete user!\n");
        break;

      case DELETE_USERS:
        List<String> userNameList = this.createUserNameList(messageContainer.messageContents);
        isSuccessful = this.deleteUsers(userNameList);
        if (isSuccessful)
        {
          returnMsg.append("Successfully delete user!\n");
          break;
        }
        returnMsg.append("Failed delete user!\n");
        break;

      case LIST_USER_DETAILS:
        user = this.getUser(messageContainer.messageContents.get(0));
        returnMsg = new StringBuilder();
        if (user != null)
        {
          isSuccessful = true;
          returnMsg.append(user.fullName + " " + user.address + " " + user.email);
          break;
        }
        returnMsg.append("user does not exist!\n");
        break;

      case LIST_ALL_USERS:
        returnMsg = new StringBuilder();
        int i = 1;
        for (String eachUserName : this.users.keySet()) {
          returnMsg.append("user " + i + ": " + eachUserName + " ");
          isSuccessful = true;
          i++;
        }
        if (!isSuccessful)
        {
          returnMsg.append("no users yet");
        }
        break;

      default:
        isHandled = false;
        System.out.println("Nothing to be done by User Manager.\n");
        break;
    }
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
    // String phoneNumber = messageContainer.messageContents.get(2);
    String address = messageContainer.messageContents.get(3);
    String email = messageContainer.messageContents.get(4);

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
      String fullName = messageContainer.messageContents.get(i) + messageContainer.messageContents.get(i + 1);
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
    for (String userName: messageContents)
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
    System.out.println("User: " + user.getName() + " added to the list of users.\n");
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
    return users.get(fullName);
  }

  /*
   * @brief Update the user with the new information
   * @param user The user information to be changed to
   * @return True or false if it was successful or not
   */
  public boolean updateUser(User user)
  {
    User userUpdate = users.get(user.fullName);
    if (users.containsKey(user.fullName))
    {
      userUpdate.address = user.address;
      userUpdate.email = user.email;
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
    boolean someDeleted = false;
    for (String userName : userNameList)
    {
      deleteUser(userName);
      someDeleted = true;
    }
    return someDeleted;
  }

  /*
   * @brief Add the associated account number to the user
   * @param user The user to have an account number added
   */
  public void addAssociatedAccountsNo(User user) 
  {
    User userNumOfAssociatedAccounts = users.get(user.fullName);
    if (user != null)
    {
      userNumOfAssociatedAccounts.numOfAssociatedAccounts += 1;
    }
  }
}
