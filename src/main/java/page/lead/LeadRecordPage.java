package page.lead;

import page.salesforce.RecordPage;
import com.codeborne.selenide.SelenideElement;
import com.sforce.soap.enterprise.sobject.Lead;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

/**
 * The Standard Salesforce page that displays Lead ({@link Lead}) record information.
 */
public class LeadRecordPage extends RecordPage {

    public final SelenideElement heading = $x("//h1[div/text()='Lead']");

    /**
     * {@inheritDoc}
     */
    public void waitUntilLoaded() {
        detailsTab.shouldBe(visible, ofSeconds(100));
        visibleLightingActionButtons.shouldHave(sizeGreaterThanOrEqual(3), ofSeconds(100));
        heading.shouldBe(visible);
    }

    /**
     * Press "Convert"/"Convert Lead" button on the Lead record page.
     * <br/><br/>
     * This method searches the button among Lightning Experience actions
     * in the upper right corner of the page
     * (even if the button is hidden in the "show more actions" list).
     */
    public void clickConvertButton() {
        clickDetailPageButton("Convert");
    }
}
