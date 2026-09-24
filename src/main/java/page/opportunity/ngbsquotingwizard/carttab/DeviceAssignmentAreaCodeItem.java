package page.opportunity.ngbsquotingwizard.carttab;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;

public class DeviceAssignmentAreaCodeItem {
    private final SelenideElement areaCodeItem;
    private final By nameElement = cssSelector(".slds-truncate");
    private final By assignedItemsInput = cssSelector("[data-ui-auto='device-assignment-area-code-quantity']");
    private final By areaCodeInput = xpath(".//div[@data-ui-auto='lookupCombobox']");
    private final By deleteButton = cssSelector("[data-ui-auto='remove-area-code-lookup']");
    private final By availableAmountText = xpath(".//div[contains(@class, 'availability')]/div");

    public DeviceAssignmentAreaCodeItem(SelenideElement areaCodeItem) {
        this.areaCodeItem = areaCodeItem;
    }

    public SelenideElement getNameElement() {
        return areaCodeItem.$(nameElement);
    }

    public SelenideElement getAvailableAmountText() {
        return areaCodeItem.$(availableAmountText);
    }

    public SelenideElement getAssignedItemsInput() {
        return areaCodeItem.$(assignedItemsInput);
    }

    public SelenideElement getAreaCodeInput() {
        return areaCodeItem.$(areaCodeInput);
    }

    public SelenideElement getDeleteButton() {
        return areaCodeItem.$(deleteButton);
    }
}
