package page.salesforce;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class SalesforcePage {

    public String salesForceTab = "//a[@title='%s']";
    public SelenideElement applicationMenu = $x("//div[@id='tsidButton']");
    public SelenideElement activeAppName = $x("//span[@id='tsidLabel']");

    //apps
    public SelenideElement currentApp = $x("//div[@title='App Menu']//span[@class='menuButtonLabel']");
    public SelenideElement serviceCloudConsoleApp = $x("//div[@class='mbrMenuItems']/a[text()='RC Service Cloud Console']");
    public SelenideElement salesCloudConsoleApp = $x("//div[@class='mbrMenuItems']/a[text()='RC Sales Console']");
    public SelenideElement rcSalesApp = $x("//div[@class='mbrMenuItems']/a[text()='RC Sales']");
    public SelenideElement serviceApp = $x("//div[@class='mbrMenuItems']/a[text()='Service']");
    //tabs
    public SelenideElement rcSalesCloudConsoleTab = $x("//li[@id='BackToServiceDesk_Tab']");
    public SelenideElement accountsTab = $x("//a[text()='Accounts']");
    public SelenideElement allTabs = $x("//a[text()='All Tabs']");
    //user navigation
    public SelenideElement userNavButton = $x("//div[@id='userNavButton']");
    public SelenideElement logoutButton = $x("//a[text()='Logout']");
}
