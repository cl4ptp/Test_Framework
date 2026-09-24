package page.opportunity.ngbsquotingwizard.producttab;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byText;

/**
 * Any single product item on the {@link ProductsPage}.
 * <br/>
 * It's represented as a single row with product's name, price, charge term,
 * and action button ("Add to Cart", "Remove from Cart"...).
 */
public class ProductItem extends TreeItem {

    //  String constants for buttons
    public static final String ADD_TO_CART_TEXT = "Add to Cart";
    public static final String ADDED_TO_CART_TEXT = "Added to Cart";
    public static final String REMOVE_FROM_CART_TEXT = "Remove to Cart";

    private final By addToCartButton = byCssSelector("[data-ui-auto-license-cart-button='add-to-cart']");
    private final By removeFromCartButton = byCssSelector("[data-ui-auto-license-cart-button='remove-from-cart']");
    private final By addedToCartButton = byCssSelector("[data-ui-auto-license-cart-button='added-to-cart']");
    private final By productName = byCssSelector("[data-ui-auto='license-item-name']");
    private final By chargeTerm = byCssSelector("[data-ui-auto='license-item-charge-term']");
    private final By price = byCssSelector("[data-ui-auto='license-item-price']");
    private final By priceToolTip = byCssSelector("[data-ui-auto='license-item-price-tooltip']");
    private final By unavailableButton = byText("Unavailable");

    /**
     * Constructor for the product item that defines its position on the web page.
     *
     * @param treeItem web element for the main container of the item
     */
    public ProductItem(SelenideElement treeItem) {
        super(treeItem.$(":scope > div:first-child"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelenideElement getNameElement() {
        return treeItem.$(productName);
    }

    /**
     * Get the value in the 'Charge term' column for the product item.
     * E.g. "Annual", "Monthly", "One - Time".
     *
     * @return web element for the value in the 'Charge term' column of the product
     */
    public SelenideElement getChargeTermElement() {
        return treeItem.$(chargeTerm);
    }

    /**
     * Get the value in the 'Price' column for the product item.
     * Usually, it's currency ISO Code + X.XX (price).
     * E.g. "USD 468.00", "EUR 8.00".
     *
     * @return web element for the value in the 'Price' column of the product
     */
    public SelenideElement getPriceElement() {
        return treeItem.$(price);
    }

    /**
     * Get the 'Add to Cart' button for the product item.
     *
     * @return web element for the 'Add to Cart' button of the product
     */
    public SelenideElement getAddToCartButtonElement() {
        return treeItem.$(addToCartButton);
    }

    /**
     * Get the 'Remove from Cart' button for the product item.
     *
     * @return web element for the 'Remove from Cart' button of the product
     */
    public SelenideElement getRemoveFromCartButtonElement() {
        return treeItem.$(removeFromCartButton);
    }

    /**
     * Get the 'Unavailable' button for the product item.
     *
     * @return web element for the 'Unavailable' button of the product
     */
    public SelenideElement getUnavailableButton() {
        return treeItem.$(unavailableButton);
    }

    /**
     * Get the 'Added to Cart' button for the product item.
     *
     * @return web element for the 'Added to Cart' button of the product
     */
    public SelenideElement getAddedToCartButtonElement() {
        return treeItem.$(addedToCartButton);
    }

    /**
     * Get the tooltip icon on the price of the product.
     * When user hovers over this icon the page displays a tooltip
     * with the price rater for the product
     * (i.e. different prices of the product depending on its quantity).
     *
     * @return web element for the tooltip icon on the price of the product
     */
    public SelenideElement getPriceToolTipElement() {
        return treeItem.$(priceToolTip);
    }
}
