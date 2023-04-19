public class User
{
  public String fullName;
  public String phoneNumber;
  public String address;
  public String email;
  public int userId = 0;
  public int numOfAssociatedAccounts = 0;

  // unused constructor?
  public User(String fullName, String address, String email)
  {
    this.fullName = fullName;
    this.address = address;
    this.email = email;
  }

  public User(String fullName, String phoneNumber, String address, String email, int userId)
  {
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.email = email;
    this.userId = userId;
  }

  public String getAddress()
  {
    return this.address;
  }

  public String getEmail()
  {
    return this.email;
  }

  public String getName()
  {
    return this.fullName;
  }

  public void changeAddress(String newAddress)
  {
    if (null == newAddress)
    {
      throw new IllegalArgumentException("null given as new address value.");
    }
    this.address = newAddress;
  }

  public void changeEmail(String newEmail)
  {
    if (null == newEmail)
    {
      throw new IllegalArgumentException("null given as new email value.");
    }
    this.email = newEmail;
  }

  public void setUserId(int id)
  {
    this.userId = id;
  }

  public int getUserId()
  {
    return this.userId;
  }
}
