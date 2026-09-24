package page.opportunity.ngbsquotingwizard.producttab;

import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;

/**
 * Products tab in the Quoting Wizard ({@link NGBSQuotingWizardPage}).
 * Used for adding products to the Cart.
 */
public class ProductsPage extends NGBSQuotingWizardPage {

    //  Tooltip message for unavailable products (Rental Phones)
    public static final String RENTAL_PHONES_UNAVAILABLE_MESSAGE =
            "Rental Phones are available only under Contract. " +
                    "Please select Contract on the Package tab.";

    public final SelenideElement searchBar = $("[data-ui-auto='search-bar']");
    public final SelenideElement productsList = $(".tree");
    public final ElementsCollection products = $$("[data-ui-auto='license-item']");
    public final ElementsCollection cboxes = $$("cbox");

    /**
     * Get Product on the Products tab by its name.
     * This method searches and expands the given cbox to find the product inside.
     * <br/>
     * Note: webdriver will also scroll to the found product.
     *
     * @param cboxName    name of product category folder (cbox),
     *                    e.g. "Phones"
     * @param productName name of given product
     *                    e.g. "Polycom VVX 501 Color Touchscreen Phone with 1 Expansion Module"
     * @return ProductItem object located in the given product category and with a given name
     */
    public ProductItem getProductByName(String cboxName, String productName) {
        if (cboxName != null && !cboxName.isBlank()) {
            var cbox = getCboxByName(cboxName);
            cbox.expand();
        }

        //  Locate web element for product item and scroll to it to make it visible in the browser
        var productItemElement = $("[data-ui-auto-license-name='" + productName + "']");
        productItemElement.scrollIntoView("{block: \"center\"}");

        return new ProductItem(productItemElement);
    }

    /**
     * Get product category (cbox) by name.
     *
     * @param cboxName name of product category
     * @return Cbox object with a displayed name
     */
    public Cbox getCboxByName(String cboxName) {
        var cboxElement = $("[data-ui-auto-cbox-name='" + cboxName + "']");
        return new Cbox(cboxElement);
    }

    /**
     * Get list of all visible products on the Products tab.
     * Note: products in the collapsed cboxes are NOT visible!
     * Make sure to expand the necessary cboxes before calling this method
     * to have the necessary products in this list.
     *
     * @return list of all Product Items that are visible to the user
     * @see #expandAllCboxes()
     */
    public List<ProductItem> getAllVisibleProducts() {
        return products.stream().map(ProductItem::new).collect(toList());
    }

    /**
     * Open Products tab by clicking on the tab's button.
     *
     * @return reference to the opened Products tab
     */
    public ProductsPage openTab() {
        productsTabButton.click();
        products.shouldHave(sizeGreaterThan(0), ofSeconds(30));
        return this;
    }

    /**
     * Find and add product to the Cart.
     *
     * @param productCbox name for the added product's category folder (cbox)
     * @param productName name for the added product
     */
    public void addProduct(String productCbox, String productName) {
        getProductByName(productCbox, productName)
                .getAddToCartButtonElement()
                .shouldBe(visible, ofSeconds(20))
                .click();
    }

    /**
     * Expand all the <b>visible</b> product category folders (cboxes).
     * Do this to make <b>most</b> products visible on the tab
     * before making assertions on "all" product items.
     */
    public void expandAllCboxes() {
        cboxes.forEach(cbox -> cbox.scrollIntoView("{block: \"center\"}").click());
    }
}
