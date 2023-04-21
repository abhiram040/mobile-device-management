import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/*
 * @brief Responsible for handling all interactions with bundles
 */
public class BundleManagement implements PropertyChangeListener
{
  Map<String, Bundle> bundles = new HashMap<>();
  private MessageContainer messageContainer;
  ServerMessageHandler serverMessageHandler;

  /*
   * @brief Default constructor
   */
  public BundleManagement()
  {
  }

  /*
   * @brief Constructor which sets the serverMessageHandler
   * @param msgHandler The server's message handler for interacting with the client
   */
  public BundleManagement(ServerMessageHandler msgHandler)
  {
    this.serverMessageHandler = msgHandler;
  }

  /*
   * @brief Sets the messageContainer and performs the requested task
   * @param evt A triggered event that AccountManagement is waiting to act on
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    this.messageContainer = (MessageContainer)evt.getNewValue();
    this.performRequestedTask();
  }

  /*
   * @brief Attempts to execute the requested task, builds the response message, and sends it
   */
  public void performRequestedTask()
  {
    String bundleName;
    String callingPlanName;
    String messagingPlanName;
    Bundle bundle;
    StringBuilder returnMsg = new StringBuilder();
    boolean isSuccessful = false;
    // case wise handling of message passed down by server
    switch(messageContainer.menuOption)
    {
      case ADD_PRE_BUNDLE:
        bundleName = messageContainer.messageContents.get(0);
        bundle = new Bundle(bundleName);
        isSuccessful = this.addBundle(bundle);
        if (isSuccessful)
        {
          returnMsg.append("Successfully added bundle to available bundle types!\n");
          break;
        }
        returnMsg.append("Failed to add bundle. Bundle either already exists or is not an offered preconfig bundle.\n");
        break;

      case ADD_PAC_BUNDLE_V1:
        bundleName = messageContainer.messageContents.get(0);
        bundle = new Bundle(bundleName);
        isSuccessful = this.addBundle(bundle);
        if (isSuccessful)
        {
          returnMsg.append("Successfully added PaC bundle!\n");
          break;
        }
        returnMsg.append("Failed to add bundle. Bundle likely already exists in available bundles.\n");
        break;

      case ADD_PAC_BUNDLE_V2:
        callingPlanName = messageContainer.messageContents.get(0);
        isSuccessful = this.addPaCWithCalling(callingPlanName);
        if (isSuccessful)
        {
          returnMsg.append("Successfully added PaC bundle with calling!\n");
          break;
        }
        returnMsg.append("Failed to add bundle. Bundle likely already exists in available bundles.\n");
        break;

      case ADD_PAC_BUNDLE_V3:
        messagingPlanName = messageContainer.messageContents.get(0);
        isSuccessful = this.addPaCWithMessaging(messagingPlanName);
        if (isSuccessful)
        {
          returnMsg.append("Successfully added PaC bundle with messaging!\n");
          break;
        }
        returnMsg.append("Failed to add bundle. Bundle likely already exists in available bundles\n");
        break;

      case LIST_BUNDLE_DETAILS:
        bundleName = messageContainer.messageContents.get(0);
        bundle = this.getBundle(bundleName);
        returnMsg = new StringBuilder();
        returnMsg.append(bundle.name + " ");
        returnMsg.append(bundle.callingPlan + " ");
        returnMsg.append(bundle.messagingPlan + " ");
        returnMsg.append(bundle.dataPlan + " ");
        returnMsg.append(bundle.monthlyFees);
        break;

      case LIST_ALL_PRE_BUNDLES:
        for (Map.Entry<String, Bundle> entry : bundles.entrySet())
        {
          bundle = entry.getValue();
          if (!isPaCBundle(bundle))
          {
            returnMsg.append(bundle.name + " ");
            returnMsg.append(bundle.monthlyFees);
          }
        }
        break;

      case LIST_ALL_PAC_BUNDLES:
        for (Map.Entry<String, Bundle> entry : bundles.entrySet())
        {
          bundle = entry.getValue();
          if (isPaCBundle(bundle))
          {
            returnMsg.append(bundle.name + " with " + bundle.callingPlan + " calling, " + bundle.messagingPlan + " messaging, " + bundle.dataPlan + " data");
            returnMsg.append(bundle.monthlyFees);       
          }
        }
        break;

      default:
        System.out.println("Nothing to be done by Bundle Manager.\n");
        break;
    }
    serverMessageHandler.buildAndSendResponseMessage(messageContainer.menuOption, isSuccessful, returnMsg.toString());
  }

  /*
   * @brief Attempts to add the specified bundle to storage
   * @param bundle The specified bundle
   * @return True or false based on if the operation was successful or not
   */
  public boolean addBundle(Bundle bundle)
  {
    if (bundle.isPaCBundle() == false && bundles.containsKey(bundle.name))
    {
      System.out.println("This type of bundle already exists in offered bundle types. Nothing done.\n");
      return false;
    }
    if (bundle.isPaCBundle() == true)
    {
      for (String bundleKeys : bundles.keySet())
      {
        if (bundles.get(bundleKeys).equals(bundle))
        {
          System.out.println("This type of bundle already exists in offered bundle types. Nothing done.\n");
          return false;
        }
      }
    }
    bundles.put(bundle.name, bundle);
    return true;
  }

  /*
   * @brief Attempts to add the specified calling plan to the bundle
   * @param callingPlan The specified calling plan
   * @return True or false based on if the operation was successful or not
   */
  public boolean addPaCWithCalling(String callingPlan)
  {
    Bundle newBundle = new Bundle("Pick and Choose");
    newBundle.setPaCCallingOption(callingPlan);
    boolean success = this.addBundle(newBundle);
    return success;
  }

  /*
   * @brief Attempts to add the specified messaging plan to the bundle
   * @param callingPlan The specified messaging plan
   * @return True or false based on if the operation was successful or not
   */
  public boolean addPaCWithMessaging(String messagingPlan)
  {
    Bundle newBundle = new Bundle("Pick and Choose");
    newBundle.setPaCMessagingOption(messagingPlan);
    boolean success = this.addBundle(newBundle);
    return success;
  }

  /*
   * @brief Retrieves the bundle based on the name
   * @param name The specified bundle that maps to the inputted name
   * @return The related bundle
   */
  public Bundle getBundle(String name)
  {
    return bundles.get(name);
  }

  /*
   * @brief Checks if the specified bundle name is registered
   * @param name The specified bundle name
   * @return True or false based on if it was found or not
   */
  public boolean isBundleRegistered(String bundleName)
  {
    if (null == this.getBundle(bundleName) || null == bundleName)
    {
      return false;
    }
    return true;
  }

  /*
   * @brief Checks if the specified bundle name is a PAC bundle
   * @param name The specified bundle name
   * @return True or false based on if it is a PAC bundle or not
   */
  public boolean isPaCBundle(Bundle bundle)
  {
    return bundle.isPaCBundle();
  }
}
