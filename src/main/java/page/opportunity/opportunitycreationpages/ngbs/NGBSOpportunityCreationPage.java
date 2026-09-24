package page.opportunity.opportunitycreationpages.ngbs;

import model.ngbs.testdata.Product;
import page.components.*;
import page.components.lookup.AngularLookupComponent;
import page.components.packageselector.PackageSelector;
import page.opportunity.OpportunityRecordTypeSelectionModal;
import page.opportunity.ngbsquotingwizard.producttab.Cbox;
import page.opportunity.opportunitycreationpages.OpportunityCreationPage;
import page.salesforce.account.AccountRecordPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;

/**
 * Quick Opportunity Creation Page. Opens after:
 * <p> 1. Clicking 'Opportunities' -> 'New' and selecting 'New Sales Opportunity'
 * type in {@link OpportunityRecordTypeSelectionModal} window</p>
 * <p> 2. Clicking on {@link AccountRecordPage} 'Opportunities' -> 'New' and selecting 'New Sales Opportunity'
 * type in {@link OpportunityRecordTypeSelectionModal} window</p>
 * <p>
 * Contains elements of QOP and methods of working with them.
 */
public class NGBSOpportunityCreationPage extends OpportunityCreationPage {

    //  Field labels
    public static final String NEW_NUMBER_OF_USERS = "New Number of Users";
    public static final String NEW_NUMBER_OF_LICENSES = "New Number of Licenses";

    //  Error messages
    public static final String CLOSE_DATE_IS_REQUIRED_ERROR = "Close Date is required";
    public static final String CLOSE_DATE_IN_THE_PAST_ERROR = "Close Date can't be in the Past";
    public static final String PROVISIONING_DETAILS_ARE_REQUIRED_FOR_DOWNSELL_ERROR =
            "Provisioning Details are required for Downsell";
    public static final String NO_PRIMARY_OR_SIGNATORY_CONTACT_ON_ACCOUNT = "You can't proceed with Opportunity Creation. " +
            "There are no Primary or Signatory Contacts under selected Account.";
    public static final String NO_ACCOUNT_SELECTED_MESSAGE = "No account selected";

    //  For 'Edition' picklist
    public static final String ESSENTIALS_PACKAGE_EDITION = "Essentials";

    //  'Enter Opportunity Info' section
    public final SelenideElement opportunityInfoSection = $x("//li[.//opportunity-info]");
    public final AngularLookupComponent accountComboboxInput = new AngularLookupComponent();
    public final SelenideElement accountInputWithSelectedValue =
            $("[data-ui-auto='defaultAccountLookupCombobox'] .slds-input");
    public final AngularLookupComponent contactLookupInput = new AngularLookupComponent(
            $("[data-ui-auto='defaultContactLookupCombobox']"));
    public final SelenideElement contactInputWithSelectedValue =
            $("[data-ui-auto='defaultContactLookupCombobox'] .slds-input");
    public final SelenideElement opportunityNameInput = $("[formcontrolname='oppName']");
    public final SelenideElement closeDateSection = $("#closed-date");
    public final SelenideElement closeDateTextInput = $("[data-ui-auto='close-date']");
    public final SelenideElement provisioningDetailsTextArea = $("[data-ui-auto='provisioning-details']");
    public final SelenideElement provisioningDetailsError = provisioningDetailsTextArea.parent().sibling(0);
    public final SelenideElement shippingAddressBox = $("[data-ui-auto='shipping-address']");
    public final SelenideElement quoteTypePicklist = $("#select-quote-type");
    public final SelenideElement errorNotificationIcon = $("notification-bar .slds-theme_error");
    public final ElementsCollection notifications = $$("notification-bar p.notification__text");
    public final Calendar calendar = new Calendar(); // for 'Close Date' input
    public final ShippingAddressForm shippingAddressForm = new ShippingAddressForm();

    //  'Select Service Plan' section
    public final SelenideElement servicePlanSection = $x("//li[.//service-plan-selector]");
    public final SelenideElement billingSystem = servicePlanSection.$(byText("Billing System")).sibling(0);
    public final SelenideElement brandPicklist = $("#select-brand-filter");
    public final ElementsCollection brandPicklistOptions = brandPicklist.$$("option");
    public final SelenideElement businessIdentityPicklist = $("#select-business-identity-filter");
    public final ElementsCollection businessIdentityPicklistOptions = businessIdentityPicklist.$$("option");
    public final SelenideElement editionPicklist = $("#select-edition-filter");
    public final ElementsCollection editionPicklistOptions = $("#select-edition-filter").$$("option");
    public final SelenideElement servicePicklist = $("#select-service-filter");
    public final ElementsCollection servicePicklistOptions = $("#select-service-filter").$$("option");
    public final SelenideElement newNumberOfDLsInput = $("[datauiauto='new-number-of-dls'] input");
    public final SelenideElement newNumberOfDLsLabel = $("[datauiauto='new-number-of-dls'] label");
    public final AreaCodeSelector defaultAreaCodeSelector = new AreaCodeSelector();

    //  Package Selector Section (inside the 'Service Plan' section)
    public final PackageSelector packageSelector = new PackageSelector();

    //  'Add Products' section
    public final SelenideElement addProductsSection = $x("//li[.//products]");
    public final ElementsCollection products = addProductsSection.$$("product");
    public final ElementsCollection cboxes = addProductsSection.$$("cbox");

    //  Other page elements
    public final SelenideElement continueToOppButton = $("#create-opportunity-action");
    public final SelenideElement spinner = $("[data-ui-auto='spinner']");

    /**
     * Get the single product object from "Add Products" section.
     *
     * @param cboxName    cbox (collapsible category) where a Product is located (e.g. "Phones")
     * @param productName name of the necessary Product (e.g. "Polycom IP 5000 Conference Phone")
     * @return row with a necessary Product name within Product box
     */
    public ProductItemQOP getProduct(String cboxName, String productName) {
        //  Expand combo-box, if necessary
        if (cboxName != null && !cboxName.isBlank()) {
            var cboxElement = $("[data-ui-auto-cbox-name='" + cboxName + "']");
            cboxElement.scrollIntoView("{block: \"center\"}");
            new Cbox(cboxElement).expand();
        }

        var productRow = $x("//cbox[@data-ui-auto-cbox-name='" + cboxName + "']" +
                "//product[.//@title='" + productName + "']");
        productRow.scrollIntoView("{block: \"center\"}");
        return new ProductItemQOP(productRow);
    }

    /**
     * Get all visible products from 'Add Products' section.
     * <br/>
     * Note: products in the collapsed Cboxes are hidden and won't get
     * in this collection.
     *
     * @return list of product rows, each with elements
     * for product's name, charge term, prices, quantities...
     */
    public List<ProductItemQOP> getAllVisibleProducts() {
        return products.stream()
                .map(ProductItemQOP::new)
                .collect(toList());
    }

    /**
     * Open "Quick Opportunity Page" by direct link.
     * <p>
     * It allows skipping:
     * <p> 1. Opening corresponding account record </p>
     * <p> 2. Clicking "New" in Opportunity section </p>
     * <p> 3. Selecting "New Sales Opportunity" in {@link OpportunityRecordTypeSelectionModal} window </p>
     *
     * @return opened NGBS Opportunity Creation Page reference
     */
    public NGBSOpportunityCreationPage openPage() {
        open(OPPORTUNITY_CREATION_PAGE_URL);
        return this;
    }

    /**
     * Open "Quick Opportunity Page" by direct link.
     *
     * It allows skipping:
     * <p> 1. Opening corresponding account record </p>
     * <p> 2. Pressing "New" in Opportunity section </p>
     * <p> 3. Selecting "New Sales Opportunity" there </p>
     * <p></p>
     * <p><b>
     * Note: main difference with {@link NGBSOpportunityCreationPage#openPage()}
     * that it allows to open QOP with preselected Account Record!
     * So there's no need to search and select it manually afterwards.
     * </b></p>
     *
     * @param accountId ID of Account for which Opportunity is created
     * @return opened NGBS Opportunity Creation Page reference
     */
    public NGBSOpportunityCreationPage openPage(String accountId) {
        open(OPPORTUNITY_CREATION_PAGE_URL + "&accid=" + accountId);
        return this;
    }

    /**
     * Populate 'Close Date' field with today's date.
     */
    public void populateCloseDate() {
        closeDateTextInput.shouldBe(visible, ofSeconds(30)).click();
        calendar.setTodayDate();
    }

    /**
     * Add products via "Add Products" section.
     *
     * @param products list of Products that should be added
     */
    public void addProducts(Product... products) {
        for (var productToAdd : products) {
            step("Add a product '" + productToAdd.name + "' to the Opportunity", () -> {
                getProduct(productToAdd.cbox, productToAdd.name)
                        .addToCart(productToAdd.quantity);
            });
        }
    }

    /**
     * <p> 1. Open Shipping Address form. </p>
     * <p> 2. Set Customer Name. </p>
     * <p> 3. Select Shipping Option. </p>
     * <p> 4. Click 'Apply' button. </p>
     */
    public void applyShippingDetails(String customerName, String shippingOption) {
        shippingAddressBox.click();
        shippingAddressForm.getCustomerName().setValue(customerName);
        shippingAddressForm.getShippingOptionPicklist().selectOption(shippingOption);
        shippingAddressForm.applyShippingForm();
    }
}
