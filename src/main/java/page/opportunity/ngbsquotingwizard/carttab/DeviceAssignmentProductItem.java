package page.opportunity.ngbsquotingwizard.carttab;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.By.cssSelector;

public class DeviceAssignmentProductItem {
    private final SelenideElement productItem;
    private final List<DeviceAssignmentAreaCodeItem> childAreaCodeItems = new ArrayList<>();
    private final By nameElement = cssSelector("div[data-ui-auto-device-assignment-product-name]");
    private final By assignedItemsInput = cssSelector("[data-ui-auto='device-assignment-area-code-quantity']");
    private final By totalNumber = cssSelector("[data-ui-auto='device-total-number']");
    private final By availableNumber = cssSelector("[data-ui-auto='device-available-number']");
    private final By addAreaCodeButton = cssSelector("c-button[iconname='add']");
    private final By deviceAssignmentAreaCodeItems = cssSelector("device-assignment-area-code");

    public DeviceAssignmentProductItem(SelenideElement productItem) {
        this.productItem = productItem;
    }

    public SelenideElement getNameElement() {
        return productItem.$(nameElement);
    }

    public SelenideElement getAssignedItemsInput() {
        return productItem.$(assignedItemsInput);
    }

    public SelenideElement getTotalNumber() {
        return productItem.$(totalNumber);
    }

    public SelenideElement getAvailableNumber() {
        return productItem.$(availableNumber);
    }

    public SelenideElement getAddAreaCodeButton() {
        return productItem.$(addAreaCodeButton);
    }

    public List<DeviceAssignmentAreaCodeItem> getChildAreaCodeItems() {
        if (productItem.$(deviceAssignmentAreaCodeItems).isDisplayed()) {
            productItem.$$(deviceAssignmentAreaCodeItems)
                    .forEach(item -> childAreaCodeItems.add(new DeviceAssignmentAreaCodeItem(item)));
        }
        return childAreaCodeItems;
    }
}
