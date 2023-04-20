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
    // case wise handling of message passed down by server
    switch(messageContainer.menuOption)
    {
      case ADD_PRE_BUNDLE:
        bundleName = messageContainer.messageContents.get(0);
        bundle = new Bundle(bundleName);
        this.addBundle(bundle);
        serverMessageHandler.sendMessage("adding preconfig bundle!\n");
        break;

      case ADD_PAC_BUNDLE_V1:
        bundleName = messageContainer.messageContents.get(0);
        bundle = new Bundle(bundleName);
        this.addBundle(bundle);
        serverMessageHandler.sendMessage("adding PaC bundle!\n");
        break;

      case ADD_PAC_BUNDLE_V2:
        callingPlanName = messageContainer.messageContents.get(0);
        this.addPaCWithCalling(callingPlanName);
        serverMessageHandler.sendMessage("adding PaC bundle with calling plan name!\n");
        break;

      case ADD_PAC_BUNDLE_V3:
        messagingPlanName = messageContainer.messageContents.get(0);
        this.addPaCWithMessaging(messagingPlanName);
        serverMessageHandler.sendMessage("adding PaC bundle with messaging plan name!\n");
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
        serverMessageHandler.sendMessage(returnMsg.toString());
        break;

      case LIST_ALL_PRE_BUNDLES:
        for (Map.Entry<String, Bundle> entry : bundles.entrySet())
        {
          bundle = entry.getValue();
          if (!isPaCBundle(bundle))
          {
            returnMsg.append(bundle.name + " ");
            returnMsg.append(bundle.monthlyFees);
            serverMessageHandler.sendMessage(returnMsg.toString());
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
            serverMessageHandler.sendMessage(returnMsg.toString());
          }
        }
        break;

      default:
        System.out.println("Nothing to be done by Bundle Manager.\n");
        break;
    }
  }

  // should be able to handle both preconfig and PaC bundle types
  public void addBundle(Bundle bundle)
  {
    bundles.put(bundle.name, bundle);
  }

  public void addPaCWithCalling(String callingPlan)
  {
    Bundle newBundle = new Bundle("Pick and Choose");
    newBundle.setPaCCallingOption(callingPlan);
    this.addBundle(newBundle);
  }

  public void addPaCWithMessaging(String messagingPlan)
  {
    Bundle newBundle = new Bundle("Pick and Choose");
    newBundle.setPaCMessagingOption(messagingPlan);
    this.addBundle(newBundle);
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
