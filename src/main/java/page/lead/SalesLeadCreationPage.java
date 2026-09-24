package page.lead;

import com.codeborne.selenide.SelenideElement;

import static utilities.Constants.BASE_VF_URL;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class SalesLeadCreationPage extends BaseLeadCreationPage {

    public SelenideElement leadSource = $x("//select[contains(@id, 'customSourceSelector')]");
    public SelenideElement salesLeadSource = $x("//select[contains(@id, 'salesleadsource')]");
    public SelenideElement agentEmailAddress = $x("//input[contains(@id, 'agentemailAddress')]");

    public SelenideElement dnisValue = $x("//span[contains(@id, 'DNIS')]");
    public SelenideElement campaignName = $x("//span[contains(@id, 'CNAME')]");

    public SelenideElement numberOfEmployeesInput = $x("//input[contains(@id, 'NumberOfEmployees')]");
    public SelenideElement numberOfEmployeesNeedingPhones = $x("//input[contains(@id, 'NumberOfEmployeesPhone')]");

    public SelenideElement industrySelect = $x("//select[contains(@id, 'industry')]");

    public SelenideElement searchButton = $x("//div[@class='btnDiv']/input[@value='Search' and @type='button']");

    public SelenideElement yesRatio = $x("//input[@value='YES']");
    public SelenideElement noRatio = $x("//input[@value='NO']");

    public SelenideElement saveButton = $x("//input[@value='Save']");

    public SelenideElement firstUnprotectedTakeOwnerShipButton = $x("//button[text()='Take Ownership']");
    public SelenideElement ownerShipConfirmation = $("#confirmationUnprotectedLeadOwnershipDivYes");

    private final SelenideElement customerInformationLabel = $x("//h2[text()='Customer Information']");

    /**
     * Constructor for Sales Lead Creation page with iframe's title.
     * Defines Sales Lead Creation page location.
     */
    public SalesLeadCreationPage() {
        super("New Sales Lead");
    }

    /**
     * Open Sales Lead Creation page via direct link using Base URL.
     * <p> Note: contents for Base URL are usually provided via system properties. </p>
     *
     * @return opened Sales Lead Creation Page reference
     */
    public SalesLeadCreationPage openPage() {
        open(BASE_VF_URL + "/apex/LeadSearchExtension");
        waitForPageLoading();
        return this;
    }

    public void waitForPageLoading() {
        customerInformationLabel.shouldBe(visible, ofSeconds(30));
    }
}
