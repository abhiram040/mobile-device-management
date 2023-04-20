public class ServiceAccount
{
  public String phoneNumber;
  public User user;
  public Bundle bundle;
  public int accountId;

  public ServiceAccount(String phoneNumber, User user, Bundle bundle)
  {
    this.phoneNumber = phoneNumber;
    this.user = user;
    this.bundle = bundle;
  }

  public ServiceAccount(String phoneNumber, User user, Bundle bundle, int accountId)
  {
    this.phoneNumber = phoneNumber;
    this.user = user;
    this.bundle = bundle;
    this.accountId = accountId;
  }

  public void changeBundle(Bundle newBundle)
  {
    if (null == newBundle)
    {
      throw new IllegalArgumentException("Cannot change account bundle to invalid value. Nothing done.\n");
    }
    this.bundle = newBundle;
  }
}
