package page.opportunity.ngbsquotingwizard.costcentertab;

import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CostCentersPage extends NGBSQuotingWizardPage {
    public static final String TOP_LEVEL_COST_CENTER_NAME = "Top Level Cost Center";

    private final SelenideElement costCenterTabBlade = $x("//blade[@name='billingCodes']");

    private final ElementsCollection costCenters = $$("billing-codes-list-item");

    private final By productNameInCostCenter = byCssSelector("* .product-name");

    //Product list (Shelf)
    public SelenideElement shelfContainer = $(".wrap-billing-codes-product-list");
    public ElementsCollection shelfProducts = $$(".product-box");
    public ElementsCollection shelfProductNames = $$(".product-box > div > div:first-child");

    //List of all CostCenters including Products
    public SelenideElement costCenterContainer = $("billing-codes-list .billing-codes-list");
    public ElementsCollection costCentersItemsContainers = $$("billing-codes-list-item .billing-code");

    public By costCenterAddLevelButton = byXpath(".//button[@title='Add Level']");
    public By costCenterLocationButton = byCssSelector("button[title='Location']");
    public By costCenterProductsContainer = byCssSelector(".billing-code-products");

    //Location Window
    public SelenideElement locationPopover = $x("//billing-codes-location-modal/section");
    public SelenideElement locationNewButton = $x("//billing-codes-location-modal//button[text()='New ']");

    public ElementsCollection locationListLineItems = $$x("//billing-codes-location-modal//li");
    public By locationsEditButton = byCssSelector("div > label > div > svg");

    public SelenideElement locationCostCenterName = $("billing-codes-location-modal #popover-location-header");
    public SelenideElement locationNameInput = $("billing-codes-location-modal #input-label");
    public SelenideElement locationStreet1Input = $("billing-codes-location-modal #input-street-1");
    public SelenideElement locationStreet2Input = $("billing-codes-location-modal #input-street2");
    public SelenideElement locationCountyInput = $("billing-codes-location-modal #input-county");
    public SelenideElement locationCityInput = $("billing-codes-location-modal #input-city");
    public SelenideElement locationStateInput = $("billing-codes-location-modal #input-state");
    public SelenideElement locationZipInput = $("billing-codes-location-modal #input-zip");
    public SelenideElement locationCountryInput = $("billing-codes-location-modal #input-country");

    public SelenideElement locationCancelButton = $x("//billing-codes-location-modal//button[text()='Cancel '']");
    public SelenideElement locationSaveButton = $x("//billing-codes-location-modal//button[text()='Save ']");

    //Footer
    public SelenideElement discardButton = costCenterTabBlade.$x(".//button[contains(text(), 'Discard')]");
    public SelenideElement saveButton = costCenterTabBlade.$x(".//button[contains(text(), 'Save')]");
    public SelenideElement uploadFileButton = costCenterTabBlade.$x(".//button[contains(text(), 'Upload file')]");

    public SelenideElement getTaxLocationEditButton(SelenideElement costCenter) {
        return costCenter.$(costCenterLocationButton);
    }

    public SelenideElement getAddLevelButton(SelenideElement costCenter) {
        return costCenter.$(costCenterAddLevelButton);
    }

    public SelenideElement getProductOnShelf(String productName) {
        return shelfContainer.$x(".//div[./div[text()=' " + productName + " ']]");
    }

    public SelenideElement getQuantityOnShelf(String productName) {
        return getProductOnShelf(productName).$x("./div/span");
    }

    public SelenideElement getCostCenterByIndex(int index) {
        assertThat(index).as("CostCenter index").isGreaterThan(0);
        if (costCenters.isEmpty()) {
            throw new AssertionError("No CostCenters are presented in a list");
        } else {
            return costCenters.get(index - 1);
        }
    }

    public SelenideElement getCostCenter(String costCenterName) {
        return $x("//div[./article/div/header/div/h2/input[@ng-reflect-model= '" + costCenterName + "']]");
    }

    public SelenideElement getProductsContainerInCostCenter(String costCenterName) {
        return getProductsContainerInCostCenter(getCostCenter(costCenterName));
    }

    public SelenideElement getProductsContainerInCostCenter(SelenideElement costCenter) {
        return costCenter.$(costCenterProductsContainer);
    }

    public SelenideElement getProductInCostCenter(String costCenterName, String productName) {
        return getProductInCostCenter(getCostCenter(costCenterName), productName);
    }

    public SelenideElement getProductInCostCenter(SelenideElement costCenter, String productName) {
        return costCenter.$x(".//billing-codes-product[.//div[text()=' " + productName + " ']]");
    }

    public SelenideElement getQuantityInputInCostCenter(String costCenterName, String productName) {
        return getQuantityInputInCostCenter(getCostCenter(costCenterName), productName);
    }

    public SelenideElement getQuantityInputInCostCenter(SelenideElement costCenter, String productName) {
        return getProductInCostCenter(costCenter, productName).$x(".//input");
    }

    public ElementsCollection getAllProductNamesInCostCenter(String costCenterName) {
        return getAllProductNamesInCostCenter(getCostCenter(costCenterName));
    }

    public ElementsCollection getAllProductNamesInCostCenter(SelenideElement costCenter) {
        return costCenter.$$(productNameInCostCenter);
    }

    public SelenideElement getCostCenterNameInput(String costCenterName) {
        return getCostCenterNameInput(getCostCenter(costCenterName));
    }

    public SelenideElement getCostCenterNameInput(SelenideElement costCenter) {
        return costCenter.$x(".//input");
    }

    public SelenideElement getDeleteButtonInCostCenter(String costCenterName, String productName) {
        return getDeleteButtonInCostCenter(getCostCenter(costCenterName), productName);
    }

    public SelenideElement getDeleteButtonInCostCenter(SelenideElement costCenter, String productName) {
        return costCenter.$x(".//button");
    }
}
