package page.opportunity.opportunitycreationpages;

import page.opportunity.OpportunityRecordPage;
import page.salesforce.VisualforcePage;

import static utilities.Constants.BASE_VF_URL;

/**
 * Common page for Opportunity creation (for both NGBS and non-NGBS brands).
 * <p></p>
 * This page can be accessed either:
 * <p> - via direct link </p>
 * <p> - from Account record page (Related list with Opportunities) </p>
 * <p> - from "Opportunity" tab </p>
 * <p></p>
 * Sales users use this page to create new Opportunities.
 * On this page, they can select account and its contact; select package;
 * set number of digital lines, add some products (phones).
 * <p>
 * When everything's set up here, user is transferred to {@link OpportunityRecordPage}
 * for this new Opportunity object.
 * </p>
 */
public abstract class OpportunityCreationPage extends VisualforcePage {
    /**
     * Direct link to Opportunity Creation page.
     * Works for both NGBS and non-NGBS brands.
     */
    protected static final String OPPORTUNITY_CREATION_PAGE_URL = BASE_VF_URL +
            "/apex/OpportunityCreationForm" +
            "?retURL=%2F006%2Fo" +
            "&RecordType=01234000000HpTN" +
            "&ent=Opportunity" +
            "&save_new=1" +
            "&sfdc.override=1";

    public OpportunityCreationPage() {
        super("New Opportunity");
    }
}
