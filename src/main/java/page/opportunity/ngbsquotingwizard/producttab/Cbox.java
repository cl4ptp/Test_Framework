package page.opportunity.ngbsquotingwizard.producttab;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selectors.byCssSelector;

/**
 * Product category folder for products on {@link ProductsPage}.
 * <br/>
 * This is an expandable folder that hides the products when collapsed.
 * Every cbox folder may contain products and other cboxes with more products.
 * Usually, products, their categories, their hierarchy comes from NGBS.
 */
public class Cbox extends TreeItem {
    public static final String INACTIVE_CBOX_STATE_CLASS = "slds-theme_info";
    public static final String ACTIVE_CBOX_STATE_CLASS = "slds-theme_success";

    private final By cboxInfoElement = byCssSelector("[data-ui-auto='cbox-item-name']");
    private final By cboxWrapper = byCssSelector(".cbox-wrapper");

    /**
     * Constructor for cbox with a given web element to locate it.
     *
     * @param treeItem web element for the cbox main container
     */
    public Cbox(SelenideElement treeItem) {
        super(treeItem);
    }

    /**
     * Expand the cbox if it's collapsed.
     *
     * @return expanded Cbox object
     */
    public Cbox expand() {
        var cboxWrapperElement = treeItem.$(cboxWrapper);
        cboxWrapperElement.shouldHave(attribute("class"));
        if (cboxWrapperElement.getAttribute("class").contains("slds-is-open")) {
            return this;
        }
        cboxWrapperElement.scrollIntoView("{block: \"center\"}").click();
        return this;
    }

    /**
     * Get the name of the given cbox.
     * Note: this web element contains 'class' attributes for cbox's state
     * (expanded, collapsed, disabled, error)
     *
     * @return web element for the name of the cbox
     */
    @Override
    public SelenideElement getNameElement() {
        return treeItem.$(cboxInfoElement);
    }
}
