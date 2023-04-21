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
      System.out.println("message container before display response: " + messageContainer.menuOption + " " + messageContainer.messageContents);
      ui.displayResponse(messageContainer);
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
