package page.lead.convert;

import com.codeborne.selenide.SelenideElement;

/**
 * Single item in the legacy 'Select Service Plan' table that can be found on {@link LeadConvertPage}
 * when Legacy Existing Business account is selected.
 * <p></p>
 * This table's item contains various information about Legacy account's package/service plan:
 * package name, service name, edition, charge term...
 */
public class LegacyServicePlanItem {
    private final SelenideElement container;

    /**
     * Class constructor.
     *
     * @param container web element that represents a "container"
     *                  for selected service plan's information (name, edition, plan...)
     */
    public LegacyServicePlanItem(SelenideElement container) {
        this.container = container;
    }

    /**
     * Return actual web element behind Service Plan item.
     * <p></p>
     * Useful if test needs to perform actions on the web element itself
     * via Selenide framework actions (waits, assertions, etc...)
     *
     * @return web element that represents service plan item.
     */
    public SelenideElement getSelf() {
        return container;
    }

    /**
     * Get service plan's name for legacy account's service plan.
     * This value is in the table's column "Name".
     *
     * @return service plan's name (e.g. "MVP Standard 2 - 99 lines")
     */
    public SelenideElement getName() {
        return container.$x(".//td[@class='cart-col--name']");
    }

    /**
     * Get service plan's service name for legacy account's service plan.
     * This value is in the table's column "Service".
     *
     * @return service plan's service name (e.g. "Office")
     */
    public SelenideElement getService() {
        return container.$x(".//td[3]");
    }

    /**
     * Get service plan's edition for legacy account's service plan.
     * This value is in the table's column "Edition".
     *
     * @return service plan's edition (e.g. "Standard", "Premium")
     */
    public SelenideElement getEdition() {
        return container.$x(".//td[4]");
    }

    /**
     * Get service plan's charge term for legacy account's service plan.
     * This value is in the table's column "Plan".
     *
     * @return service plan's name (e.g. "Annual", "Monthly")
     */
    public SelenideElement getChargeTerm() {
        return container.$x(".//td[6]");
    }
}
