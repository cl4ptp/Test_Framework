package page.components;

import page.opportunity.ngbsquotingwizard.quotetab.QuotePage;
import page.opportunity.opportunitycreationpages.ngbs.NGBSOpportunityCreationPage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byTitle;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

/**
 * Calendar component used to set some date fields like 'Close date' or 'Default Start Day'
 * <p></p>
 * Can be found on {@link NGBSOpportunityCreationPage}, {@link QuotePage}
 */
public class Calendar {

    //  Container
    private final SelenideElement datePickerContainer = $x("//ngl-datepicker");

    //  Buttons
    public final SelenideElement previousMonthButton = datePickerContainer.$(byTitle("Previous Month"));
    public final SelenideElement nextMonthButton = datePickerContainer.$(byTitle("Next Month"));
    public final SelenideElement todayButton = datePickerContainer.$(byText("Today"));

    public final SelenideElement monthTableHeader = datePickerContainer.$("h2");
    public final SelenideElement yearPicklist = datePickerContainer.$("ngl-date-year select");
    public final SelenideElement daysTable = datePickerContainer.$(".datepicker__month");

    /**
     * Populate today's date in calendar
     */
    public void setTodayDate() {
        todayButton.shouldBe(visible, ofSeconds(10)).click();
    }

    /**
     * Populate the date selected from params
     *
     * @param day   Day of month to be selected in calendar
     * @param month Month to be selected in calendar
     * @param year  Year to be selected in calendar
     */
    public void setDate(String day, String month, String year) {
        while (!monthTableHeader.getText()
                .equalsIgnoreCase(month)) {
            nextMonthButton.click();
        }
        yearPicklist.selectOptionByValue(year);

        daysTable.find(byText(day)).click();
    }
}
