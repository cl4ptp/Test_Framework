package page.opportunity.opportunitycreationpages.legacy;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class LegacyOpportunityCreationPageProductRow {

    private final SelenideElement parentElement;

    private final By buttonProductExpand = By.xpath(".//td[@class='product-details']/button");
    private final By previouslySold = By.xpath(".//div[@class='service-plan-icon cTooltip']");
    private final By productName = By.xpath(".//div[@title='Product Name']");
    private final By category = By.xpath(".//div[@title='Category']");
    private final By plan = By.xpath(".//div[@title='Plan']");
    private final By inputCheckBox = By.xpath(".//td[@class='col__selected']//div[@class='slds-truncate']//lightning-input");


    public LegacyOpportunityCreationPageProductRow(SelenideElement parentElement) {
        this.parentElement = parentElement;
    }

    public LegacyOpportunityCreationPageProductRow expand() {
        parentElement.$(buttonProductExpand).click();
        return this;
    }

    public boolean isPreviouslySold() {
        return parentElement.$(previouslySold).exists();
    }

    public SelenideElement getProductName() {
        return parentElement.$(productName);
    }

    public SelenideElement getCategory() {
        return parentElement.$(category);
    }

    public SelenideElement getPlan() {
        return parentElement.$(plan);
    }

    public void switchState() {
        parentElement.$(inputCheckBox).click();
    }

    public boolean isSelected() {
        return parentElement.getAttribute("class").contains("slds-is-selected");
    }
}
