public class ServerMessageHandlerBuilder
{
  private int port;

  public ServerMessageHandlerBuilder port(int port)
  {
    this.port = port;
    return this;
  }

  public ServerMessageHandler build()
  {
    return new ServerMessageHandler(this.port);
  }
}
