import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagement implements PropertyChangeListener
{
  Map<String, User> users = new HashMap<>();
  private MessageContainer messageContainer;

  ServerMessageHandler serverMessageHandler;

  public UserManagement()
  {

  }

  public UserManagement(ServerMessageHandler msgHandler)
  {
    this.serverMessageHandler = msgHandler;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    System.out.println("user management activity");
    this.messageContainer = (MessageContainer)evt.getNewValue();
    this.performRequestedTask();
  }

  public void performRequestedTask()
  {
    User user;
    List<User> userList = new ArrayList<>();
    StringBuilder returnMsg = new StringBuilder();
    boolean isSuccessful = false;
    switch(messageContainer.menuOption)
    {
      case ADD_USER:
        user = this.createUser(messageContainer.messageContents);
        this.addUser(user);

        break;

      case ADD_USERS:
        userList = this.createUserList(messageContainer.messageContents);
        this.addUsers(userList);
        
        break;

      case UPDATE_USER:
        user = this.createUser(messageContainer.messageContents);
        this.updateUser(user);

        break;

      case DELETE_USER:
        String userName = messageContainer.messageContents.get(0);
        this.deleteUser(userName);

        break;

      case DELETE_USERS:
        List<String> userNameList = this.createUserNameList(messageContainer.messageContents);
        this.deleteUsers(userNameList);
        
        break;

      case LIST_USER_DETAILS:
        user = this.getUser(messageContainer.messageContents.get(0));
        returnMsg = new StringBuilder();
        returnMsg.append(user.fullName + " " + user.address + " " + user.email);

        break;

      case LIST_ALL_USERS:
        returnMsg = new StringBuilder();
        for (String eachUserName : this.users.keySet()) {
          returnMsg.append(eachUserName + " ");
        }

        break;

      default:
        System.out.println("Nothing to be done by User Manager.\n");
        break;
      
    }
    // TODO: I'd suggest designing the code so this is the only message call.
    // ie. Return the output of a task to performRequestedTask and then send it here.
    serverMessageHandler.buildAndSendResponseMessage(messageContainer.menuOption, isSuccessful, returnMsg.toString());
  }

  public User createUser(List<String> messageContents) {
    String fullName = messageContainer.messageContents.get(0) 
              + " " + messageContainer.messageContents.get(1);
    // String phoneNumber = messageContainer.messageContents.get(2);
    String address = messageContainer.messageContents.get(3);
    String email = messageContainer.messageContents.get(4);

    return new User(fullName, address, email);
  }

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

  public List<String> createUserNameList(List<String> messageContents) {
    List<String> userNameList = new ArrayList<>();
    for (String userName: messageContents) {
      userNameList.add(userName);
    }

    return userNameList;
  }

  public void addUser(User user)
  {
    if (this.users.containsKey(user.getName()))
    {
      System.out.println("Cannot add: " + user.getName() + " because they already exist in the system.\n");
      //serverMessageHandler.sendMessage("Cannot add: " + user.getName() + " because they already exist in the system.\n");
      return;
    }
    users.put(user.fullName, user);
    System.out.println("User" + user.getName() + " added to the list of users.\n");
    //serverMessageHandler.sendMessage("User" + user.getName() + " added to the list of users.\n");
    
    //TODO: Centralize the response handling, use buildAndSendResponseMessage instead of sendMessage();
  }

  public void addUsers(List<User> userList)
  {
    for (User user : userList)
    {
      if (user.getUserId() == 0)
      {
        user.setUserId(users.size() + 1);
      }
      addUser(user);
    }
  }

  public User getUser(String fullName)
  {
    return users.get(fullName);
  }

  public void updateUser(User user)
  {
    User userUpdate = users.get(user.fullName);
    if (user != null)
    {
      userUpdate.address = user.address;
      userUpdate.email = user.email;
    }
  }

  public void deleteUser(String fullName)
  {
    System.out.println("WARNING: Removing user: " + fullName + " and all associated accounts.\n");
    users.remove(fullName);
  }

  public void deleteUsers(List<String> userNameList)
  {
    for (String userName : userNameList)
    {
      deleteUser(userName);
    }
  }
}
