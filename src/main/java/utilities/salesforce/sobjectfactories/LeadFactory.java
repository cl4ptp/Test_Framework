package utilities.salesforce.sobjectfactories;

import utilities.salesforce.sobjecthelper.LeadHelper;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.soap.enterprise.sobject.User;

import java.util.UUID;

/**
 * Factory class for creating quick instances of {@link Lead} class.
 * <br/>
 * All factory methods also insert created objects into the SF database.
 */
public class LeadFactory extends SObjectFactory {
    //  Default values for lead's parameters
    private static final String DEFAULT_LEAD_NAME = "TestLead";
    private static final String DEFAULT_ACCOUNT_NAME = "TestAccount";

    /**
     * Create new Customer Lead record and insert it into Salesforce via API.
     *
     * @param ownerUser user to be set as Lead's owner
     * @return Lead record with default parameters and ID from Salesforce
     * @throws Exception in case of malformed query, DB or network errors.
     */
    public static Lead createCustomerLeadInSFDC(User ownerUser) throws Exception {
        return createLeadInSFDC(ownerUser, false);
    }

    /**
     * Create new Partner Lead record and insert it into Salesforce via API.
     *
     * @param ownerUser user to be set as Lead's owner
     * @return Lead record with default parameters and ID from Salesforce
     * @throws Exception in case of malformed query, DB or network errors.
     */
    public static Lead createPartnerLeadInSFDC(User ownerUser) throws Exception {
        return createLeadInSFDC(ownerUser, true);
    }

    /**
     * Create new Lead record and insert it into Salesforce via API.
     *
     * @param ownerUser user to be set as Lead's owner
     * @param isPartner true, if method should create a Partner Lead,
     *                  false - for Customer Leads.
     * @return Lead record with default parameters and ID from Salesforce
     * @throws Exception in case of malformed query, DB or network errors.
     */
    private static Lead createLeadInSFDC(User ownerUser, boolean isPartner) throws Exception {
        var lead = new Lead();
        lead.setFirstName(DEFAULT_LEAD_NAME);

        var uniqueId = UUID.randomUUID().toString();
        lead.setLastName(uniqueId);
        lead.setCompany(DEFAULT_ACCOUNT_NAME + " " + uniqueId);
        lead.setEmail(uniqueId + "@example.com");

        if (isPartner) {
            LeadHelper.setPartnerLeadRecordType(lead);
            lead.setLeadPartnerID__c(uniqueId);
        }

        CONNECTION_UTILS.insertAndGetIds(lead);

        lead.setOwnerId(ownerUser.getId());
        CONNECTION_UTILS.update(lead);

        return lead;
    }
}
