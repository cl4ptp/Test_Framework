package page.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

/**
 * Standard Combobox element (dropdown list of options / "picklist") from Lightning Web Components.
 *
 * @see <a href='https://developer.salesforce.com/docs/component-library/bundle/lightning-combobox'>
 * lightning-combobox documentation</a>
 */
public class LightningCombobox {
    private final SelenideElement comboboxContainer;

    /**
     * Constructor for LWC Combobox (picklist).
     *
     * @param comboboxLabel label element for the picklist
     *                      (usually used in front of the dropdown as its name)
     */
    public LightningCombobox(String comboboxLabel) {
        this.comboboxContainer =
                $x("//lightning-combobox[./label[contains(text(), '" + comboboxLabel + "')]]");
    }

    /**
     * Get input element for the combobox ("button" tag in the layout).
     * Can be used to evaluate the selected option or the state (enabled/disabled).
     *
     * @return input element for the combobox
     */
    public SelenideElement getInput() {
        return comboboxContainer.$x(".//lightning-base-combobox//button");
    }

    /**
     * Get available options for selection.
     * <br/>
     * Note: make sure to click on {@link #getInput()} first to make the available options visible.
     *
     * @return collection of the web elements for options to select
     */
    public ElementsCollection getOptions() {
        return comboboxContainer.$$x(".//lightning-base-combobox-item");
    }

    /**
     * Select an option from the dropdown.
     *
     * @param option option that is to be selected in the combobox
     */
    public void selectOption(String option) {
        getInput().click(usingJavaScript());
        getOptions().findBy(attribute("data-value", option))
                .shouldBe(visible, ofSeconds(10))
                .click();
    }
}
