package page.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;

/**
 * Standard Datepicker element (a text input to capture a date) from Lightning Web Components.
 *
 * @see <a href='https://www.lightningdesignsystem.com/components/datepickers/'>
 * Datepickers documentation</a>
 */
public class LightningDatepicker {
    private final SelenideElement datepickerElement;

    /**
     * Constructor that defines the datepicker in the DOM.
     *
     * @param datepickerElement web element for the date picker
     *                          (usually, an element with a "lightning-datepicker" tag)
     */
    public LightningDatepicker(SelenideElement datepickerElement) {
        this.datepickerElement = datepickerElement;
    }

    /**
     * Get input element for entering a date or making an assertion on the existing value.
     *
     * @return SelenideElement that represents input part of the component
     */
    public SelenideElement getInput() {
        return datepickerElement.$x(".//input");
    }

    /**
     * Get the text element for displaying error message.
     *
     * @return SelenideElement that represents a text of the error message for the component
     */
    public SelenideElement getErrorMessage() {
        return datepickerElement.$x("./*[@data-error-message]");
    }

    /**
     * Set current date in the datepicker (today).
     */
    public void setCurrentDate() {
        datepickerElement.click();
        datepickerElement.$(byText("Today")).click();
    }
}
