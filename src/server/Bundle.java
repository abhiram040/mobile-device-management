import java.util.List;

public class Bundle
{
  public String name;
  public String callingPlan;
  public String messagingPlan;
  public String dataPlan;
  public double monthlyFees;
  public static List<String> preConfiguredBundleNames;
  public static List<String> pacBundleNames;

  // General Bundle constructor that takes bundle name and sets defaults for that bundle type
  public Bundle(String name)
  {
    setPredConfiguredBundleNames();
    setPacBundleNames();
    switch (name)
    {
      case "Platinum":
        this.name = "Platinum Bundle";
        this.callingPlan = "Unlimited US & Canada wide calling";
        this.messagingPlan = "Unlimited Messages";
        this.dataPlan = "10 GB";
        this.monthlyFees = 100;
        return;
      case "Gold":
        this.name = "Gold Bundle";
        this.callingPlan = "Unlimited Canada wide calling";
        this.messagingPlan = "10K Messages";
        this.dataPlan = "4 GB";
        this.monthlyFees = 80;
        return;
      case "Silver":
        this.name = "Silver Bundle";
        this.callingPlan = "100 min free Canada wide calling";
        this.messagingPlan = "5K Messages";
        this.dataPlan = "2 GB";
        this.monthlyFees = 45;
        return;
      case "Bronze":
        this.name = "Bronze Bundle";
        this.callingPlan = "30 min free Canada wide calling";
        this.messagingPlan = "250 Messages";
        this.dataPlan = "1 GB";
        this.monthlyFees = 25;
        return;
      case "Pick and Choose":
        this.name = name;
        this.callingPlan = "Zero min";
        this.messagingPlan = "Zero messages";
        this.dataPlan = "0 GB";
        this.monthlyFees = 10;
        return;
      default:
        throw new IllegalArgumentException("Illegal Argument - Not a recognized bundle name.");
    }
  }

  private void setPredConfiguredBundleNames() {
    preConfiguredBundleNames.add("Platinum");
    preConfiguredBundleNames.add("Gold");
    preConfiguredBundleNames.add("Silver");
    preConfiguredBundleNames.add("Bronze");
  }

  private void setPacBundleNames() {

  }

  public void setPaCCallingOption(String optionName)
  {
    if (this.name != "Pick and Choose")
    {
      System.out.println("Cannot modify preconfigured bundle options. No changes made.");
      return;
    }

    switch (optionName)
    {
      case "Platinum":
        this.callingPlan = "Unlimited US & Canada wide calling";
        this.monthlyFees += 40;
        return;
      case "Gold":
        this.callingPlan = "Unlimited Canada wide calling";
        this.monthlyFees += 30;
        return;
      case "Silver":
        this.callingPlan = "100 min free Canada wide calling";
        this.monthlyFees += 20;
        return;
      case "Bronze":
        this.callingPlan = "30 min free Canada wide calling";
        this.monthlyFees += 15;
        return;
      default:
        System.out.println("Not a valid PaC calling plan. This account still has the default 0 min.");
    }
  }

  public void setPaCMessagingOption(String optionName)
  {
    if (this.name != "Pick and Choose")
    {
      System.out.println("Cannot modify preconfigured bundle options. No changes made.");
      return;
    }
    
    switch (optionName)
    {
      case "Platinum":
        this.messagingPlan = "Unlimited Messaging";
        this.monthlyFees += 45;
        return;
      case "Gold":
        this.messagingPlan = "10K Messages";
        this.monthlyFees += 35;
        return;
      case "Silver":
        this.messagingPlan = "5K Messages";
        this.monthlyFees += 25;
        return;
      case "Bronze":
        this.messagingPlan = "250 Messages";
        this.monthlyFees += 20;
        return;
      default:
        System.out.println("Not a valid PaC messaging plan. This account still has the default 0 messages.");
    }
  }

  public void setPaCDataPlan(String optionName)
  {
    if (this.name != "Pick and Choose")
    {
      System.out.println("Cannot modify preconfigured bundle options. No changes made.");
      return;
    }

    switch (optionName)
    {
      case "Platinum" :
        this.dataPlan = "10 GB";
        this.monthlyFees += 40;
        return;
      case "Gold" :
        this.dataPlan = "7 GB";
        this.monthlyFees += 30;
        return;
      case "Silver" :
        this.dataPlan = "4 GB";
        this.monthlyFees += 25;
        return;
      case "Bronze" :
        this.dataPlan = "2 GB";
        this.monthlyFees += 20;
        return;
      default:
        System.out.println("Not a valid PaC data plan. This account still has the default 0 GB.");
    }
  }

  public boolean isPaCBundle()
  {
    if (this.name != "Pick and Choose")
    {
      return false;
    }
    return true;
  }

  public void getBundleDetails()
  {
    System.out.println("Bundle type is " + name);
    System.out.println("Calling plan is: " + callingPlan);
    System.out.println("Messaging plan is: " + messagingPlan);
    System.out.println("Data plan is: " + dataPlan);
    System.out.println("Monthly total fees for this plan are: " + Double.toString(monthlyFees));
  }
}
