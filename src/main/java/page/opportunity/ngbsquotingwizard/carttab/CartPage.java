package page.opportunity.ngbsquotingwizard.carttab;

import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import page.opportunity.ngbsquotingwizard.modal.PromotionsManagerModal;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;

/**
 * Cart tab in {@link NGBSQuotingWizardPage}
 * that contains a list of Cart Items ({@link CartItem}) with their details (price, discount, quantity);
 * some useful Totals fields; additional buttons for adding taxes or applying promo discounts.
 */
public class CartPage extends NGBSQuotingWizardPage {

    //  Error messages for validations
    public static final String QUANTITY_CANNOT_BE_LOWER_THAN_ERROR = "Quantity cannot be lower than 1";
    public static final String QUANTITY_CANNOT_BE_HIGHER_THAN_ERROR = "Quantity cannot be higher than ";
    public static final String NEW_QUANTITY_CANNOT_BE_HIGHER_THAN_ERROR = "New Quantity cannot be higher than ";

    //  Text from Cart tab hints
    public static final String PROVISION_TOGGLE_HINT_TEXT = "If Provision toggle is ON then Phone Number area codes will be required. " +
            "If the toggle is OFF Phone Number will not be assigned during signup. " +
            "Provision toggle will be set to OFF automatically if total DL count is 400 or more and " +
            "for all Agent Supported countries.";

    public final SelenideElement loadingMessage = $("cart").$(byText("loading..."));

    //  Cart items
    public final ElementsCollection visibleCartItems = $$("[data-ui-auto-license-is-visible='true']");
    public final ElementsCollection allCartItemElements = $$("[data-ui-auto='cart-item']");
    public final ElementsCollection cartItemNames = $$("[data-ui-auto='cart-item-product-name']");
    public final ElementsCollection taxCartItems = $$x("//*[@data-ui-auto='cart-item'][.//div[text()='Tax']]");

    //  LBO Toggle section
    public final SelenideElement provisionToggle = $("[data-ui-auto='lbo-toggle'] .slds-checkbox_faux");
    public final SelenideElement provisionToggleInfoIcon = $("[data-ui-auto='lbo-toggle-disabled-descr']");

    //  Cart Totals
    public final SelenideElement oneTimeTotal = $("#one-time-totals .total-currency");
    public final SelenideElement newMonthlyRecurringCharges = $("#new-recurring-charges .total-currency");
    public final SelenideElement currentMonthlyRecurringCharges = $("#current-recurring-charges .total-currency");
    public final SelenideElement changeInRecurringCharges = $("#recurring-changes .total-currency");
    public final SelenideElement newMonthlyDiscount = $("#new-discount .total-currency");
    public final SelenideElement currentMonthlyDiscount = $("#current-discount .total-currency");
    public final SelenideElement changeInDiscount = $("#discount-changes .total-currency");

    //  Footer buttons
    public final SelenideElement discardButton = $("[data-ui-auto='discard-cart']");
    public final SelenideElement saveButton = $("[data-ui-auto='save-cart']");
    public final SelenideElement addTaxesButton = $("[data-ui-auto='add-taxes']");
    public final SelenideElement removeTaxes = $("[data-ui-auto='remove-taxes']");
    public final SelenideElement promosButton = $("[data-ui-auto='show-promo-modal']");

    //  Promotions modal
    public final PromotionsManagerModal promosModal = new PromotionsManagerModal();

    /**
     * Get Quote Line Item on the Cart Tab by its name.
     * <br/>
     * Note: method will return the first <i>visible</i> cart item with a given display name.
     *
     * @param licenseDisplayName displayed name of the item
     * @return composite object to extract other parameters from (quantity, price, discount...)
     */
    public CartItem getQliFromCartByDisplayName(String licenseDisplayName) {
        var cartItemSelector = String.format(
                "[data-ui-auto-license-display-name='%s'][data-ui-auto-license-is-visible='true']",
                licenseDisplayName);
        return new CartItem($(cartItemSelector));
    }

    /**
     * Get all items that are visible in the cart as collection of {@link CartItem} elements.
     * <br/>
     * Some Quote Line Items might be on the quote, but not visible in the cart (they're not on the list).
     *
     * @return list of Cart Item objects for further work with its elements
     */
    public List<CartItem> getAllVisibleCartItems() {
        return visibleCartItems.stream().map(CartItem::new).collect(toList());
    }

    /**
     * Get all Tax items in the cart as a collection of {@link CartItem} elements.
     * <br/>
     * These items appear in the cart when user presses 'Add Taxes' button on Cart tab.
     * Visually, Tax items have an additional text tag = 'Tax'.
     *
     * @return list of Cart Item objects for further work with its elements
     */
    public List<CartItem> getAllTaxCartItems() {
        return taxCartItems.stream().map(CartItem::new).collect(toList());
    }

    /**
     * Open Cart tab by clicking on the tab's button.
     *
     * @return reference to the opened Cart tab
     */
    public CartPage openTab() {
        cartTabButton.click();
        loadingMessage.shouldBe(hidden, ofSeconds(60));
        visibleCartItems.shouldHave(sizeGreaterThan(0), ofSeconds(30));
        unsavedChangesDialog.backdrop.shouldNotBe(visible);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void waitUntilLoaded() {
        spinner.shouldBe(hidden, ofSeconds(60));
        //  Additional waits are added because sometimes the spinner appears, disappears, and immediately reappears again
        sleep(1_000);
        spinner.shouldBe(hidden, ofSeconds(60));
        spinnerContainer.shouldBe(hidden, ofSeconds(60));

        //  Wait for cart items to rearrange in DOM
        loadingMessage.shouldBe(hidden, ofSeconds(60));
        visibleCartItems.shouldHave(sizeGreaterThan(0), ofSeconds(30));
    }

    /**
     * Set value for 'Quantity' field for Quote Line Item on Cart Tab.
     *
     * @param qliName  name for Quote Line Item as it displays in the Cart
     * @param quantity quantity value to set in 'Quantity' field
     */
    public void setQuantityForQLItem(String qliName, int quantity) {
        getQliFromCartByDisplayName(qliName)
                .getQuantityInput()
                .shouldBe(enabled, ofSeconds(60))
                .setValue(Integer.toString(quantity))
                .pressTab();
    }

    /**
     * Set value for 'New Quantity' field for Quote Line Item on Cart Tab.
     *
     * @param qliName     name for Quote Line Item as it displays in the Cart
     * @param newQuantity quantity value to set in 'New Quantity' field
     */
    public void setNewQuantityForQLItem(String qliName, int newQuantity) {
        getQliFromCartByDisplayName(qliName)
                .getNewQuantityInput()
                .shouldBe(enabled, ofSeconds(60))
                .setValue(Integer.toString(newQuantity))
                .pressTab();
    }

    /**
     * Set value for 'Discount' field for Quote Line Item on Cart Tab.
     *
     * @param qliName  name for Quote Line Item as it displays in the Cart
     * @param discount discount string value to set in 'Discount' field
     */
    public void setDiscountForQLItem(String qliName, int discount) {
        getQliFromCartByDisplayName(qliName)
                .getDiscountInput()
                .shouldBe(enabled, ofSeconds(60))
                .setValue(Integer.toString(discount))
                .pressTab();
    }

    /**
     * Set value for 'Discount Type' field for Quote Line Item on Cart Tab.
     *
     * @param qliName      name for Quote Line Item as it displays in the Cart
     * @param discountType discount type string value to select in 'Discount Type' field's pick-list
     */
    public void setDiscountTypeForQLItem(String qliName, String discountType) {
        //  Sometimes without the click on the pick-list, the selected value won't change with the action below
        getQliFromCartByDisplayName(qliName)
                .getDiscountTypeSelect()
                .shouldBe(enabled, ofSeconds(60))
                .click();

        getQliFromCartByDisplayName(qliName)
                .getDiscountTypeSelect()
                .selectOptionContainingText(discountType);
    }

    /**
     * Press 'Save' button on the Cart Tab of Quoting Wizard.
     */
    public void saveCart() {
        saveButton.scrollIntoView(true).click();

        spinner.shouldBe(visible);
        waitUntilLoaded();
    }

    /**
     * Add taxes to the Cart.
     * <br/>
     * Added taxes appear as additional products (Quote Line Items) in the cart.
     */
    public void addTaxes() {
        addTaxesButton.scrollIntoView(true).click();

        spinner.shouldBe(visible);
        waitUntilLoaded();
    }

    /**
     * Apply promotion using its promo code to the products in the Cart.
     * <br/>
     * Note: use it when no promotion is applied yet.
     *
     * @param promoCode Promo Code to be applied (e.g. "QA-AUTO-DL-USD", "NYPROMO2").
     * @see #changeAppliedPromo(String)
     */
    public void applyPromoCode(String promoCode) {
        promosButton.scrollIntoView(true).click();
        promosModal.clickApplyPromoButton(promoCode);
        promosModal.submitButton.click();
    }

    /**
     * Remove applied promo and apply specified promo.
     * <br/>
     * Note: use it when there's already a promotion applied.
     *
     * @param promoCode Promo Code to be applied (e.g. "QA-AUTO-DL-USD", "NYPROMO2").
     * @see #applyPromoCode(String)
     */
    public void changeAppliedPromo(String promoCode) {
        promosButton.scrollIntoView(true).click();
        promosModal.removeButton.click();
        promosModal.clickApplyPromoButton(promoCode);
        promosModal.submitButton.click();
    }
}
