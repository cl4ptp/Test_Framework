package page.opportunity.closewizard;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class DownsellWizardPage {
    public static final String WHAT_S_CHANGING = "What's changing";
    public static final String CHURN_AND_SUB_CHURN_REASONS = "Churn and Sub Churn reasons";
    public static final String WHAT_WILL_CUSTOMER_USE_INSTEAD = "What will customer use instead";

    public SelenideElement spinner = $(".cSpinner");
    public SelenideElement cancelButton = $x("//button[text()='Cancel']");
    public SelenideElement submitButton = $x("//button[text()='Submit']");

    public SelenideElement whatsChangingBlock = $x("//p[text()=\"" + WHAT_S_CHANGING + "\"]/ancestor::div[@data-aura-class='cOptionsPanel']");
    public SelenideElement churnAndSubChurnReasonsBlock = $x("//p[text()='" + CHURN_AND_SUB_CHURN_REASONS + "']/ancestor::div[@data-aura-class='cOptionsPanel']");
    public SelenideElement whatWillCustomerUseInsteadBlock = $x("//p[text()='" + WHAT_WILL_CUSTOMER_USE_INSTEAD + "']/ancestor::div[@data-aura-class='cOptionsPanel']");

    public SelenideElement getCheckBoxOptionByName(String name) {
        return $x("//span[text()='" + name + "']/preceding-sibling::span");
    }

    public SelenideElement getRootOptionInBlockByName(SelenideElement optionsBlock, String name) {
        return optionsBlock.$x(".//p[span[text()='" + name + "']]");
    }

    //this method works both for expandable options and radio-buttons
    public SelenideElement getExpandableOptionListElementByName(SelenideElement optionsBlock, String name) {
        return optionsBlock.$x(".//label[span[text()='" + name + "']]");
    }

    public SelenideElement getTextInputOther(SelenideElement optionsBlock) {
        return optionsBlock.$(".cOptionsPanelAdditionalItem > div > div > input");
    }
}
