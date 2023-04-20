import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class BundleManagement implements PropertyChangeListener
{
  Map<String, Bundle> bundles = new HashMap<>();
  private MessageContainer messageContainer;

  ServerMessageHandler serverMessageHandler;

  public BundleManagement()
  {

  }

  public BundleManagement(ServerMessageHandler msgHandler)
  {
    this.serverMessageHandler = msgHandler;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    this.messageContainer = (MessageContainer)evt.getNewValue();
    this.performRequestedTask();
  }

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
    // TODO: I'd suggest designing the code so this is the only message call.
    // ie. Return the output of a task to performRequestedTask and then send it here.
    serverMessageHandler.buildAndSendResponseMessage(messageContainer.menuOption, isSuccessful, returnMsg.toString());
  }

  // should be able to handle both preconfig and PaC bundle types
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

  public boolean addPaCWithCalling(String callingPlan)
  {
    Bundle newBundle = new Bundle("Pick and Choose");
    newBundle.setPaCCallingOption(callingPlan);
    boolean success = this.addBundle(newBundle);
    return success;
  }

  public boolean addPaCWithMessaging(String messagingPlan)
  {
    Bundle newBundle = new Bundle("Pick and Choose");
    newBundle.setPaCMessagingOption(messagingPlan);
    boolean success = this.addBundle(newBundle);
    return success;
  }

  public Bundle getBundle(String name)
  {
    return bundles.get(name);
  }

  public boolean isPaCBundle(Bundle bundle)
  {
    return bundle.isPaCBundle();
  }
}
