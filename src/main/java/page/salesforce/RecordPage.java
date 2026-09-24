package page.salesforce;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.SObject;

import static utilities.Constants.BASE_URL;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static java.time.Duration.ofSeconds;

/**
 * Abstract record page that defines common elements for a typical Salesforce record page:
 * Account, Contact, Opportunity, Lead, etc...
 */
public abstract class RecordPage extends SalesforcePage {

    public static final String NEW_BUTTON_LABEL = "New";

    public final SelenideElement visibleRecordPageContainer = $(".windowViewMode-normal");

    //  Highlights panel
    public final SelenideElement highlightsPanel = visibleRecordPageContainer.$("records-lwc-highlights-panel");
    public final SelenideElement entityTitle = highlightsPanel.$x(".//*[contains(@class, 'entityNameTitle')]");
    public final SelenideElement highlightsPanelButtonsSection =
            highlightsPanel.$x(".//*[contains(@class, 'primaryFieldRow')]");
    public final ElementsCollection visibleLightingActionButtons =
            highlightsPanel.$$x(".//*[@class='slds-button-group-list']/*[@class='visible']");
    public final SelenideElement moreActionsButton =
            highlightsPanel.$x(".//button[span='Show more actions']");

    //  Tabs
    public SelenideElement detailsTab = $x("//li[@title='Details']");
    public SelenideElement relatedTab = $x("//li[@title='Related']");

    //  Notification block
    private final SelenideElement notificationContainer = $(".slds-notify_container");
    public final SelenideElement notification = notificationContainer.$("[data-aura-class='forceActionsText']");
    public final SelenideElement notificationCloseButton = notificationContainer.$("button[title='Close']");

    //  'Details' section
    public final SelenideElement detailsSection = $x("//records-lwc-detail-panel");
    public final ElementsCollection recordSections = visibleRecordPageContainer
            .$$("[class*='section-header-title']");

    public final ElementsCollection relatedLists = $$x("//lst-related-list-single-container");

    /**
     * Get ID for the record from the currently opened record page.
     *
     * @return standard Salesforce 18-character ID (e.g. "0031k00000c4bSHAAY") for the record.
     */
    public String getCurrentRecordId() {
        var result = url().split("/")[6];

        if (result.length() != 15 && result.length() != 18) {
            throw new IllegalCallerException("This is not a record page -> " + url());
        }
        return result;
    }

    /**
     * Get the related list on the record page.
     *
     * @param relatedListName name for the related list as it appears in the UI
     *                        (e.g. "Opportunities", "Related Contacts", etc...)
     * @return web element for a container with a related list
     */
    public SelenideElement getRelatedListByName(String relatedListName) {
        return $x("//lst-related-list-single-container" +
                "[.//span='" + relatedListName + "']");
    }

    /**
     * Open the Record page for the given SObject.
     *
     * @param sObject SObject to open a record page for
     * @return reference to the opened Record Page
     */
    public RecordPage openPage(SObject sObject) {
        return openPage(sObject.getId());
    }

    /**
     * Open the Record page for the given SObject.
     *
     * @param sObjectId ID of the SObject to open a record page for
     * @return reference to the opened Record Page
     */
    public RecordPage openPage(String sObjectId) {
        open(BASE_URL + "/" + sObjectId);
        waitUntilLoaded();

        return this;
    }

    /**
     * Wait until the page loads most of its important elements.
     * User may safely interact with any of the page's elements after this method is finished.
     */
    public void waitUntilLoaded() {
        //  provide the implementation in child classes
    }

    /**
     * Click on the record's action button.
     * <br/><br/>
     * This method searches the button among Lightning Experience actions
     * in the upper right corner of the page
     * (even if the button is hidden in the "show more actions" list).
     *
     * @param buttonLabel button's label as shown in UI
     *                    (e.g. "Close", "Delete", "Edit", etc...)
     *                    <b> Note: method works with partial labels too:
     *                    e.g. "Convert" works for "Convert Lead" button! </b>
     */
    public void clickDetailPageButton(String buttonLabel) {
        executeJavaScript("window.scrollTo(0, 0)");

        //  Additional wait for all the LE action buttons to arrange
        moreActionsButton.shouldBe(visible, ofSeconds(20));
        sleep(3_000);

        var detailPageButton = highlightsPanelButtonsSection.$(withText(buttonLabel));
        if (!detailPageButton.isDisplayed()) {
            moreActionsButton.hover().click();
        }
        detailPageButton.click();
    }

    /**
     * Click on the visible List Button on the related list.
     *
     * @param relatedListName name for the related list as it appears in the UI
     *                        (e.g. "Opportunities", "Related Contacts", etc...)
     * @param buttonLabel     button's label as shown in UI
     *                        (e.g. "New", "Edit", etc...)
     */
    public void clickVisibleListButtonOnRelatedList(String relatedListName, String buttonLabel) {
        var relatedListElement = getRelatedListByName(relatedListName);
        relatedListElement.$(byText(buttonLabel)).shouldBe(visible, ofSeconds(10)).click();
    }

    /**
     * Click on the List Button on the related list
     * that's hidden in the 'show more actions' dropdown list.
     *
     * @param relatedListName name for the related list as it appears in the UI
     *                        (e.g. "Opportunities", "Related Contacts", etc...)
     * @param buttonLabel     button's label as shown in UI
     *                        (e.g. "New", "Edit", etc...)
     */
    public void clickHiddenListButtonOnRelatedList(String relatedListName, String buttonLabel) {
        var relatedListElement = getRelatedListByName(relatedListName);
        //  Press 'Show more actions' to see dropdown list with hidden actions
        relatedListElement.$x(".//*[@class='actionsWrapper' or contains(@class, 'actionsContainer')]")
                .$x(".//*[@type='button' or @role='button']")
                .shouldBe(visible, ofSeconds(10))
                .hover()
                .click();
        $x("//a[.//*[text()='" + buttonLabel + "'] and @role='menuitem']").click();
    }

    /**
     * Open the related list on the record page.
     * The resulting page is a separate list view with related records and action buttons.
     *
     * @param relatedListName name for the related list as it appears in the UI
     *                        (e.g. "Opportunities", "Related Contacts", etc...)
     */
    public void openRelatedList(String relatedListName) {
        getRelatedListByName(relatedListName).$x(".//h2/a").click();
    }
}
