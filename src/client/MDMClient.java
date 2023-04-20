public class MDMClient
{
  private static UI ui;
  private static ClientMessageHandler clientMessageHandler;

  private static void clientLoop()
  {
    ui.printUI();
    String messageToServer = ui.retrieveUserInput();

    if (!messageToServer.isEmpty())
    {
      clientMessageHandler.sendMessage(messageToServer);
      MessageContainer messageContainer = clientMessageHandler.retrieveMessage();
      ui.displayResponse(messageContainer);
    }
    else
    {
       System.out.println("Error: Invalid message from the server");
    }
  }

  public static void main(String[] args) 
  {
    ui = new UI();
    clientMessageHandler = new ClientMessageHandler();

    while (true)
    {
      clientLoop();
    }
  }
}
