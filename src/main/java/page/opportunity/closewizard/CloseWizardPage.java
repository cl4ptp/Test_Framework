package page.opportunity.closewizard;

import page.salesforce.IframePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

public class CloseWizardPage extends IframePage {

    public CloseWizardPage() {
        super("CloseOpportunityWizardInline");
    }

    public static final String WHO_DID_WE_BEAT_WINDOW_TEXT = "Who did/will we beat?";
    public static final String WHO_DID_WE_REPLACE_WINDOW_TEXT = "Who did/will we replace?";
    public static final String WHAT_IS_THE_PRIMARY_WIN_REASON_WINDOW_TEXT = "What is the Primary Reason we won/will win?";

    public SelenideElement spinner = $(".cSpinner");
    public SelenideElement editButton = $x("//button[text()='Edit']");
    public SelenideElement cancelButton = $x("//button[text()='Cancel']");
    public SelenideElement submitButton = $x("//button[text()='Submit']");

    public SelenideElement whoDidWeBeatBlock = $x("//p[text()='" + WHO_DID_WE_BEAT_WINDOW_TEXT + "']/ancestor::div[@data-aura-class='cOptionsPanel']");
    public SelenideElement whoDidWeReplaceBlock = $x("//p[text()='" + WHO_DID_WE_REPLACE_WINDOW_TEXT + "']/ancestor::div[@data-aura-class='cOptionsPanel']");
    public SelenideElement whatIsThePrimaryWinReasonBlock = $x("//p[text()='" + WHAT_IS_THE_PRIMARY_WIN_REASON_WINDOW_TEXT + "']/ancestor::div[@data-aura-class='cOptionsPanel']");

    public SelenideElement getOptionsBlockByName(String name) {
        return $x("//p[text()='" + name + "']/ancestor::div[@data-aura-class='cOptionsPanel']");
    }

    public SelenideElement getRootOptionInBlockByName(SelenideElement optionsBlock, String name) {
        return optionsBlock.$x(".//p[span[text()='" + name + "']]");
    }

    //this method works both for expandable options and radio-buttons
    public SelenideElement getExpandableOptionListElementByName(SelenideElement optionsBlock, String name) {
        return optionsBlock.$x(".//label[span[text()='" + name + "']]");
    }

    public SelenideElement getTextInputOther(SelenideElement optionsBlock) {
        return optionsBlock.$(".cOptionsPanelItem > div > div > div > div > input");
    }

    public void submitCloseWizard() {
        //  fill 'Who did We beat' block
        whoDidWeBeatBlock.shouldBe(visible, ofSeconds(100));
        getRootOptionInBlockByName(whoDidWeBeatBlock, "Other").click();
        getExpandableOptionListElementByName(whoDidWeBeatBlock, "Other").click();
        getTextInputOther(whoDidWeBeatBlock).setValue("Closed by an automated test");

        //  fill 'Who did We replace' block
        whoDidWeReplaceBlock.shouldBe(visible, ofSeconds(30));
        getRootOptionInBlockByName(whoDidWeReplaceBlock, "Other").click();
        getExpandableOptionListElementByName(whoDidWeReplaceBlock, "Other").click();
        getTextInputOther(whoDidWeReplaceBlock).setValue("Closed by an automated test");

        //  fill 'What is the Primary Win Reason' block
        whatIsThePrimaryWinReasonBlock.shouldBe(visible, ofSeconds(30));
        getExpandableOptionListElementByName(whatIsThePrimaryWinReasonBlock, "Channel Relationship").click();

        submitButton.shouldBe(visible, ofSeconds(10)).click();
    }
}
