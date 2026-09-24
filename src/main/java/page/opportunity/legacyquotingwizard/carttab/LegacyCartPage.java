package page.opportunity.legacyquotingwizard.carttab;

import page.opportunity.legacyquotingwizard.LegacyQuotingWizardPage;
import com.codeborne.selenide.ElementsCollection;

import static utilities.StringHelper.EMPTY_STRING;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * 'Cart' page: one of the tabs on the Legacy Quote Wizard pipeline.
 * <br/><br/>
 * Can be accessed via Quote Wizard, 'Cost Center' and 'ProServ Quote' tabs
 * ({@link LegacyQuotingWizardPage}).
 * <br/><br/>
 * Contains items that were added on the 'Products' tab with their prices, discounts,
 * and other information.
 */
public class LegacyCartPage extends LegacyQuotingWizardPage {
    public final ElementsCollection quoteLineItemListEntries = $$("tr.cQuotingToolCartListEntry");

    /**
     * Get Quote Line Item on the Cart Tab by its name.
     *
     * @param name displayed name of the item (e.g. "Contact Center: Basic Edition Seat")
     * @return composite object to extract other parameters from (quantity, price, discount...)
     */
    public LegacyCartItem getQliFromCart(String name) {
        return getQliFromCart(name, EMPTY_STRING);
    }

    /**
     * Get Quote Line Item on the Cart Tab by its name and its family name.
     *
     * @param name   displayed name of the item (e.g. "Contact Center: Basic Edition Seat")
     * @param family displayed family name of the item (e.g. "CC Service")
     * @return composite object to extract other parameters from (quantity, price, discount...)
     */
    public LegacyCartItem getQliFromCart(String name, String family) {
        return new LegacyCartItem(
                $x("//tr[contains(@class,'cQuotingToolCartListEntry') " +
                        "and ./td[contains(@class,'cart__name') " +
                        "and ./div[contains(.,'" + name + "')] " +
                        "and ./div[contains(.,'" + family + "')]" +
                        "]]"
                )
        );
    }
}
