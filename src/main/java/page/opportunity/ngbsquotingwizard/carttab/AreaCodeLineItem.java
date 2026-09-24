package page.opportunity.ngbsquotingwizard.carttab;

import model.ngbs.testdata.AreaCode;
import page.components.AreaCodeSelector;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;

public class AreaCodeLineItem {
    private final SelenideElement areaCodeLineItem;
    private final By areaCodeQuantityInput = cssSelector("input[type='number']");
    private final By deleteAreaCodeButton = cssSelector("[iconname='delete'] button");
    private final By addMoreAreaCodeButton = cssSelector("c-button[ng-reflect-icon-name='add'] button");
    private final By areaCodeSearchInput = cssSelector("input[placeholder='Start typing to search for Area Codes']");
    private final By availableAmountText = xpath(".//div[contains(@class, 'availability')]/div");
    public final AreaCodeSelector areaCodeSelector = new AreaCodeSelector();

    public AreaCodeLineItem(SelenideElement areaCodeLineItem) {
        this.areaCodeLineItem = areaCodeLineItem;
    }

    public SelenideElement getQuantityInput() {
        return areaCodeLineItem.$(areaCodeQuantityInput);
    }

    public SelenideElement getAvailableAmountText() {
        return areaCodeLineItem.$(availableAmountText);
    }

    public SelenideElement getDeleteButton() {
        return areaCodeLineItem.$(deleteAreaCodeButton);
    }

    public SelenideElement getAddMoreButton() {
        return areaCodeLineItem.$(addMoreAreaCodeButton);
    }

    public void setAreaCode(AreaCode areaCode) {
        areaCodeSelector.selectCode(areaCode);
    }
}
