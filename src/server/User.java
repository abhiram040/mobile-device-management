/*
 * @brief Responsible for storing the relevant user information
 */
public class User
{
  public String fullName;
  public String phoneNumber;
  public String address;
  public String email;
  public int numOfAssociatedAccounts = 0;

  /*
   * @brief Constructor that sets the specified information
   * @param fullName The specified full name
   * @param address The specified address
   * @param email The specified email
   */
  public User(String fullName, String address, String email)
  {
    this.fullName = fullName;
    this.address = address;
    this.email = email;
  }

  /*
   * @brief Constructor that sets the specified information
   * @param fullName The specified full name
   * @param phoneNumber The specified phone number
   * @param address The specified address
   * @param email The specified email
   */
  public User(String fullName, String phoneNumber, String address, String email)
  {
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.email = email;
  }

  /*
   * @brief Getter for the address
   * @return The address of the user
   */
  public String getAddress()
  {
    return this.address;
  }

  /*
   * @brief Getter for the email
   * @return The email of the user
   */
  public String getEmail()
  {
    return this.email;
  }

  /*
   * @brief Getter for the name
   * @return The name of the user
   */
  public String getName()
  {
    return this.fullName;
  }

  /*
   * @brief Sets the address of the user to the new specified address
   * @param newAddress The new address of the user
   */
  public void changeAddress(String newAddress)
  {
    if(null == newAddress)
    {
      throw new IllegalArgumentException("null given as new address value.");
    }
    this.address = newAddress;
  }

  /*
   * @brief Sets the email of the user to the new specified email
   * @param newEmail The new email of the user
   */
  public void changeEmail(String newEmail)
  {
    if(null == newEmail)
    {
      throw new IllegalArgumentException("null given as new email value.");
    }
    this.email = newEmail;
  }
  
  /*
   * @brief Used to easily print the string representation of the class
   * @param The constructed string representation of this class
   */
  @Override
  public String toString() {
    return "User: " + this.fullName + " Addres: " + this.address + " Email: " + this.email;
  }
}
