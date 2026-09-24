package utilities.salesforce.sobjecthelper;

import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.ws.ConnectionException;

import java.util.Random;

/**
 * Helper class to facilitate operations on {@link Account} objects.
 */
public class AccountHelper extends SObjectHelper {
    //  SFDC API parameters
    private static final String S_OBJECT_API_NAME = "Account";
    private static final String CUSTOMER_ACCOUNT_RECORD_TYPE = "Customer Account";

    //  Default values for Billing Address fields
    private static final String DEFAULT_BILLING_STREET = "516 Walden Dr";
    private static final String DEFAULT_BILLING_CITY = "Beverly Hills";
    private static final String DEFAULT_BILLING_STATE = "CA";
    private static final String DEFAULT_BILLING_POSTAL_CODE = "90210";
    public static final String DEFAULT_BILLING_COUNTRY = "United States";

    public static final String PAID_RC_ACCOUNT_STATUS = "Paid";


    /**
     * Set internal record type for the Account object with Customer Account developer/record type's name.
     *
     * @param account Account object to set up Record type on
     * @throws ConnectionException in case of errors while accessing API
     */
    public static void setCustomerAccountRecordType(Account account) throws ConnectionException {
        var customerAccountRecordTypeId = CONNECTION_UTILS.getRecordTypeId(S_OBJECT_API_NAME, CUSTOMER_ACCOUNT_RECORD_TYPE);
        account.setRecordTypeId(customerAccountRecordTypeId);
    }

    /**
     * Set up Account's billing address fields with some default values.
     *
     * @param account Account object to set up
     */
    public static void setDefaultBillingAddress(Account account) {
        account.setBillingStreet(DEFAULT_BILLING_STREET);
        account.setBillingCity(DEFAULT_BILLING_CITY);
        account.setBillingState(DEFAULT_BILLING_STATE);
        account.setBillingPostalCode(DEFAULT_BILLING_POSTAL_CODE);
        account.setBillingCountry(DEFAULT_BILLING_COUNTRY);
    }

    /**
     * Set unique 'RC_User_ID__c' for provided Account object.
     *
     * @param account account to set up
     */
    public static void setRandomEnterpriseAccountId(Account account) {
        var randomValue = new Random().nextInt(99_999_999);
        var uniqueUserId = String.format("0000%08d0000", randomValue);
        account.setRC_User_ID__c(uniqueUserId);
    }

    /**
     * Extract primary Contact object from the Account object.
     *
     * @param account Account to get primary Contact from
     * @return Contact object which is primary for its Account
     */
    public static Contact getPrimaryContactOnAccount(Account account) throws Exception {
        return CONNECTION_UTILS.querySingleRecord(
                "SELECT Id, FirstName, LastName, Email, Phone, Preferred_Language__c " +
                        "FROM Contact " +
                        "WHERE Id IN (" +
                        "SELECT ContactId " +
                        "FROM AccountContactRole " +
                        "WHERE AccountId = '" + account.getId() + "' " +
                        "AND isPrimary = true" +
                        ")",
                Contact.class);
    }
}
