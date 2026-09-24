package utilities.salesforce.sobjecthelper;

import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.ws.ConnectionException;

/**
 * Helper class to facilitate operations on {@link Lead} objects.
 */
public class LeadHelper extends SObjectHelper {
    //  SFDC API parameters
    private static final String S_OBJECT_API_NAME = "Lead";
    private static final String PARTNER_LEAD_RECORD_TYPE = "Partner Leads";

    /**
     * Set 'Partner Leads' record type for the Lead object.
     *
     * @param lead Lead object to set up Record type on
     * @throws ConnectionException in case of errors while accessing API
     */
    public static void setPartnerLeadRecordType(Lead lead) throws ConnectionException {
        var partnerLeadRecordTypeId = CONNECTION_UTILS.getRecordTypeId(S_OBJECT_API_NAME, PARTNER_LEAD_RECORD_TYPE);
        lead.setRecordTypeId(partnerLeadRecordTypeId);
    }
}
