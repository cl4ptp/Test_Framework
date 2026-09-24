package page.opportunity.modal;

import page.opportunity.OpportunityRecordPage;
import page.salesforce.GenericSalesforceModal;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$x;
import static java.time.Duration.ofSeconds;

/**
 * Modal window in {@link OpportunityRecordPage}
 * activated by clicking on 'Sync with NGBS' button.
 */
public class SyncWithNGBSModal extends GenericSalesforceModal {

    //  Sync messages
    public static final String ALL_DISCOUNTS_REMOVED_MESSAGE = "All discounts removed.\n" +
            "Terminating contracts from Salesforce is not supported right now.\n" +
            "Contact NGBS Team to remove contract.";
    public static final String SYNC_WITH_NGBS_NOT_AVAILABLE_ERROR = "Sync with NGBS not available\n" +
            "Opportunity should be in Closed Won stage before using Sync with NGBS";
    public static final String SYNC_WITH_NGBS_SUCCESS_MESSAGE = "Sync With NGBS\n" +
            "Sync Success";
    public static final String CONTRACTS_SUCCESSFULLY_SENT_MESSAGE = "Sync Contract\n" +
            "Contracts were successfully sent to the billing system";
    public static final String CONTRACT_CANCELLATION_SUCCESS_MESSAGE = "Contract cancellation succeeded";
    public static final String DISCOUNTS_SUCCESSFULLY_SYNCED_MESSAGE = "Sync Discount\n" +
            "Discounts successfully sent to the billing system.";
    public static final String SYNC_WITH_NGBS_PRICE_CHANGE_SUCCESS_MESSAGE = "Sync with NGBS\n" +
            "Price has been successfully changed";
    public static final String DISCOUNTS_ALREADY_SYNCED_ERROR = "Discounts already synced\n" +
            "Discounts for this quote were already synced";
    public static final String SYNC_IS_NOT_REQUIRED_FOR_NEW_BUSINESS_ERROR = "Sync with NGBS not available\n" +
            "Sync with NGBS is not required for new customers";
    public static final String SYNC_IS_NOT_REQUIRED_FOR_VODAFONE_ERROR =
            "Sync is not required for Vodafone Business with RingCentral\n" +
            "Please proceed with manual package change in BAP";
    public static final String SYNC_NOT_AVAILABLE_BILLING_ID_IS_EMPTY_ERROR = "Sync with NGBS not available\n" +
            "Billing ID is empty on the Account. Please wait for the Billing ID to sync on the Account record";
    public static final String YOU_HAVE_NO_PERMISSION_TO_REPRICE_ERROR = "You don't have a permission to apply reprice\n" +
            "Please contact your manager";
    public static final String YOU_HAVE_NO_PERMISSION_TO_SYNC_ERROR = "Sync with NGBS not available\n" +
            "You don't have permission to use Sync with NGBS. Please contact Sales Ops for assistance";
    public static final String LICENSES_SHOULD_BE_ADDED_MANUALLY_IN_SW_ERROR = "Error\n" +
            "Licenses are not synced with Service Web. Licenses should be added manually in Service Web.";
    public static final String NOTHING_TO_SYNC_MESSAGE = "Info\nNothing to sync";

    public static final String CONTRACTS_AND_DISCOUNTS_WILL_BE_SYNCED_MESSAGE = "Up-sell\n" +
            "Contract and Discounts will be synced with NGBS";
    public static final String DISCOUNTS_WILL_BE_SYNCED_MESSAGE = "Discount Sync\n" +
            "Discounts will be synced with NGBS";
    public static final String DISCOUNTS_WILL_BE_SYNCED_UPSELL_MESSAGE = "Up-sell\n" +
            "Discounts will be synced with NGBS";
    public static final String CONTRACTS_AND_DISCOUNTS_WILL_BE_SYNCED_UPGRADE_UPSELL_MESSAGE = "Upgrade with Up-sell\n" +
            "Contract and Discounts for the new package will be synced with NGBS";
    public static final String DISCOUNTS_WILL_BE_SYNCED_UPGRADE_UPSELL_MESSAGE = "Upgrade with Up-sell\n" +
            "Discounts for the new package will be synced with NGBS";
    public static final String CONTRACTS_AND_DISCOUNTS_WILL_BE_SYNCED_UPGRADE_DOWNSELL_MESSAGE = "Upgrade with Down-sell\n" +
            "Contract and Discounts for the new package will be synced with NGBS";
    public static final String DISCOUNTS_WILL_BE_SYNCED_UPGRADE_DOWNSELL_MESSAGE = "Upgrade with Down-sell\n" +
            "Discounts for the new package will be synced with NGBS";
    public static final String DISCOUNTS_WILL_BE_SYNCED_UPGRADE_UPSELL_DOWNSELL_MESSAGE = "Upgrade with Up-sell & Down-sell\n" +
            "Discounts for the new package will be synced with NGBS";
    public static final String CONTRACTS_AND_DISCOUNTS_WILL_BE_SYNCED_UPGRADE_UPSELL_DOWNSELL_MESSAGE = "Upgrade with Up-sell & Down-sell\n" +
            "Contract and Discounts for the new package will be synced with NGBS";

    public final SelenideElement cancelButton = dialogContainer.$(withText("Cancel"));
    public final SelenideElement nextButton = dialogContainer.$(withText("Next"));
    public final SelenideElement skipButton = dialogContainer.$(withText("Skip"));
    public final SelenideElement spinner = dialogContainer.$x(".//lightning-spinner");
    public final SelenideElement summaryText = dialogContainer.$x(".//div[@class='right slds-border_left']/div/div");

    public final SelenideElement alertNotificationBlock = dialogContainer.$x(".//div[@role='status' and contains(@class, 'error')]");
    public final SelenideElement notification = dialogContainer.$x(".//div[@role='status']//div[@class='slds-notify__content']");
    public final SelenideElement internalNotification = dialogContainer.$x(".//div[@role='status']//div[@class='slds-media__body']");
    public final SelenideElement alertCloseButton = $x("//div[@role='status' and contains(@class, 'error')]//button");
    public final ElementsCollection syncSteps = dialogContainer.$$x(".//c-sn-progress-item//*[@class='step-label']");

    public static final String DISCOUNT_SYNC_STEP = "Discount sync";
    public static final String CONTRACT_SYNC_STEP = "Contract sync";
    public static final String CONTRACT_CANCEL_STEP = "Contract cancellation";
    public static final String REPRICE_STEP = "Reprice";
    public static final String UPGRADE_STEP = "Upgrade (in external system)";
    public static final String UP_SELL_STEP = "Up-sell (in external system)";
    public static final String DOWN_SELL_STEP = "Down-sell (in external system)";

    /**
     * Constructor for the modal window to locate it via its default header.
     */
    public SyncWithNGBSModal() {
        super("Sync With NGBS");
    }

    /**
     * Get the final info message when the sync process is completed successfully.
     * The actual message depends on the active user.
     *
     * @param userFullName full name of the current user (e.g. "John Smith")
     * @return info message after successful synchronization with NGBS
     */
    public static String getSyncCompletedMessage(String userFullName) {
        return String.format("Sync was completed successfully by %s manually. Press Finish to close this window",
                userFullName);
    }

    /**
     * Click on the 'Next' button to move to the next step in the sync process.
     */
    public void clickNext() {
        nextButton.click();
        spinner.shouldBe(visible);
        spinner.shouldBe(hidden, ofSeconds(60));
    }
}
