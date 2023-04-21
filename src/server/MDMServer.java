public class MDMServer
{
  private static ServerMessageHandler serverMessageHandler;
  private static ResponseHandler responseHandler;

  private static UserManagement userManagement;
  private static BundleManagement bundleManagement;
  private static AccountManagement accountManagement;

  private static void serverLoop()
  {
    responseHandler.handleResponse();
  }

  public static void main(String[] args) 
  {
    serverMessageHandler = new ServerMessageHandler();
    responseHandler = new ResponseHandler(serverMessageHandler);

    userManagement = new UserManagement(serverMessageHandler);
    bundleManagement = new BundleManagement(serverMessageHandler);
    accountManagement = new AccountManagement(serverMessageHandler);

    responseHandler.addPropertyChangeListener(userManagement);
    responseHandler.addPropertyChangeListener(bundleManagement);
    responseHandler.addPropertyChangeListener(accountManagement);

    accountManagement.setMangerReferences(bundleManagement, userManagement);

    while (true)
    {
      serverLoop();
    }
  }
}
