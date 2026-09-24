package page.opportunity.opportunitycreationpages.ngbs;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;

/**
 * Single item row in the table of products on {@link NGBSOpportunityCreationPage}.
 * It contains elements for product's name, charge term, prices, quantities.
 */
public class ProductItemQOP {

    private final SelenideElement productElement;
    private final By newQtyInput = cssSelector(".new-quantity-input input");
    private final By addButton = cssSelector("[id^='add-checkbox']");

    /**
     * Constructor for the product item.
     *
     * @param productElement parent web element for the product item
     */
    public ProductItemQOP(SelenideElement productElement) {
        this.productElement = productElement;
    }

    /**
     * Return web element for 'New Qty' (new quantity) input field.
     *
     * @return web element for 'New Qty' input
     */
    public SelenideElement getNewQtyInput() {
        return this.productElement.$(newQtyInput);
    }

    /**
     * Add a product with a quantity = 1.
     *
     * @return current product item
     */
    public ProductItemQOP addToCart() {
        productElement.$(newQtyInput).click();
        return this;
    }

    /**
     * Add a product with a specified new quantity.
     *
     * @param quantity any positive new quantity for the product to add
     * @return current product item
     */
    public ProductItemQOP addToCart(int quantity) {
        addToCart();
        productElement.$(newQtyInput).setValue(String.valueOf(quantity)).pressTab();
        return this;
    }
}
