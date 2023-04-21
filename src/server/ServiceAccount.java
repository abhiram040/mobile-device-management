/*
 * @brief Responsible for storing relevant service account information
 */
public class ServiceAccount
{
  public String phoneNumber;
  public User user;
  public Bundle bundle;
  public int accountId;

  /*
   * @brief Constructor to create a serviceAccount with the specified inputs
   * @param phoneNumber The specified phone number
   * @param user The specified user
   * @param bundle The specified bundle
   */
  public ServiceAccount(String phoneNumber, User user, Bundle bundle)
  {
    this.phoneNumber = phoneNumber;
    this.user = user;
    this.bundle = bundle;
  }

  /*
   * @brief Constructor to create a serviceAccount with the specified inputs
   * @param phoneNumber The specified phone number
   * @param user The specified user
   * @param bundle The specified bundle
   * @param accountId The specified account ID
   */
  public ServiceAccount(String phoneNumber, User user, Bundle bundle, int accountId)
  {
    this.phoneNumber = phoneNumber;
    this.user = user;
    this.bundle = bundle;
    this.accountId = accountId;
  }

  /*
   * @brief Change the service account's current bundle to the specified bundle
   * @param newBundle The new bundle to tie to the service account
   */
  public void changeBundle(Bundle newBundle)
  {
    if (null == newBundle)
    {
      throw new IllegalArgumentException("Cannot change account bundle to invalid value. Nothing done.\n");
    }
    this.bundle = newBundle;
  }
}
