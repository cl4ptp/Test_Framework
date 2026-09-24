package page.salesforce.approval;

import page.salesforce.VisualforcePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

/**
 * Custom VF page located on the {@link TaxExemptApprovalPage}.
 * Displays information related to Tax Exempt Approvals,
 * e.g. GST Number, SEZ Certificate...
 */
public class TaxExemptionManagerPage extends VisualforcePage {

    public final SelenideElement header = $x("//*[@class='slds-page-header']//h1");
    public final SelenideElement shimmer = $x("//c-placeholder-loading");

    public final SelenideElement gstNumberInput = $x("//*[contains(@id,'gstNumber')]");
    public final SelenideElement sezCertificateLink = $x("//*[label[text()='SEZWOP Certificate']]//a");

    /**
     * Constructor with a default web element for the page's iframe.
     */
    public TaxExemptionManagerPage() {
        super($x("//records-record-layout-section[.//span[text()='Tax Exemptions']]//iframe"));
    }

    /**
     * Wait until the page is fully loaded.
     * User may safely interact with any of the page's elements after this method is finished.
     */
    public void waitUntilLoaded() {
        header.shouldBe(visible, ofSeconds(30));
        shimmer.shouldBe(hidden, ofSeconds(10));
    }
}
