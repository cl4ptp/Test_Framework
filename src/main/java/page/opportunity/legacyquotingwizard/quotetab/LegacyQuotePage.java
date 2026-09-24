package page.opportunity.legacyquotingwizard.quotetab;

import page.opportunity.legacyquotingwizard.LegacyQuotingWizardPage;
import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.valueOf;
import static java.time.Duration.ofSeconds;

/**
 * 'Quote' page: one of the tabs on the Legacy Quote Wizard pipeline.
 * <br/><br/>
 * Can be accessed via Legacy Quote Wizard 'ProServ Quote' tab ({@link LegacyQuotingWizardPage}),
 * not represented on 'Contact Center' tab.
 * <br/><br/>
 * Contains some useful fields and picklists for ProServ Quote.
 */
public class LegacyQuotePage extends LegacyQuotingWizardPage {

    //  Picklists default values
    public static final String BUDGETARY_FORECAST_CATEGORY = "Budgetary";
    public static final String CC_DIGITAL_COMPLEXITY = "CC-Digital";

    private final SelenideElement proServArchitectInput =
            $x("//*[./label[text()='ProServ Architect']]//input");
    private final SelenideElement proServProjectComplexity =
            $x("//*[@name='proServProjectComplexity']");
    private final SelenideElement saveQuoteButton = $x("//button[text()='Save' and @id='saveButton']");
    public final SelenideElement originalSOWQuoteNumberInput =
            $x("//*[./label[text()='Original SOW Quote Number']]//input");
    public final SelenideElement proServUsers = $x("//*[@name='numberOfProServUsers']");
    public final SelenideElement expirationDateInput =
            $x("//label[./span[text()='Expiration Date']]/..//input");
    public final SelenideElement proServForecastedCloseDateInput =
            $x("//label[./span[text()='ProServ Forecasted Close Date']]/..//input");
    public final SelenideElement proServForecastCategorySelect =
            $x("//*[./label[text()='ProServ Forecast Category']]//select");
    public final SelenideElement syncToPrimaryQuoteButton = $(byText("Sync To Primary Quote"));

    /**
     * Enter provided search query in the 'ProServ Architect' field and select found element from
     * the drop-down list element.
     *
     * @param searchQuery name of the searched ProServ Architect user.
     */
    public void selectProServArchitect(String searchQuery) {
        proServArchitectInput.setValue(searchQuery);
        $x("//*[@data-aura-class='cLookupItemList']//span[./span[text()='" + searchQuery + "']]")
                .click();
    }

    /**
     * Set tomorrow's date value to 'ProServ Forecasted Close Date' field.
     */
    public void selectDefaultForecastedCloseDate() {
        var forecastedCloseDate = LocalDate.now().plusDays(1);
        proServForecastedCloseDateInput.click();
        legacyDatePicker.setDate(
                valueOf(forecastedCloseDate.getDayOfMonth()),
                valueOf(forecastedCloseDate.getMonth()),
                valueOf(forecastedCloseDate.getYear()));
    }

    /**
     * Set 'CC-Digital' value to 'ProServ Project Complexity' field.
     */
    public void selectDefaultProjectComplexity() {
        proServProjectComplexity.click();
        $(byText(CC_DIGITAL_COMPLEXITY))
                .shouldBe(visible, ofSeconds(10))
                .click();
    }

    /**
     * Click 'Save' button on the ProServ Quote tab.
     */
    public void saveQuote() {
        saveQuoteButton.click();
        waitUntilLoaded();
    }

    /**
     * Click 'Sync To Primary Quote' button on the ProServ Quote tab.
     */
    public void syncProServQuoteWithPrimary() {
        syncToPrimaryQuoteButton.click();
        waitUntilLoaded();
    }
}
