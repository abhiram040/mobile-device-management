public class User
{
  public String fullName;
  public String phoneNumber;
  public String address;
  public String email;
  public int numOfAssociatedAccounts = 0;

  // unused constructor?
  public User(String fullName, String address, String email)
  {
    this.fullName = fullName;
    this.address = address;
    this.email = email;
  }

  public User(String fullName, String phoneNumber, String address, String email)
  {
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.email = email;
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
}
