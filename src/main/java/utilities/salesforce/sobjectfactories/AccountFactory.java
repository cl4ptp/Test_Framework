package utilities.salesforce.sobjectfactories;

import com.sforce.soap.enterprise.sobject.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static utilities.ServiceUtils.shortDelay;
import static utilities.salesforce.sobjectfactories.AccountContactRoleFactory.createPrimarySignatoryContactRole;
import static utilities.salesforce.sobjectfactories.ContactFactory.createContactForAccount;
import static utilities.salesforce.sobjecthelper.AccountHelper.*;

/**
 * Factory class for creating quick instances of {@link Account} class
 * with/without dependent objects (e.g. {@link Contact}, {@link AccountContactRole} etc.).
 * <p>
 * All factory methods also insert created objects into the SF database.
 */
public class AccountFactory extends SObjectFactory {
    //  Default values to include in Account.Name field
    private static final String NEW_CUSTOMER_DEFAULT_ACCOUNT_NAME = "NGBS New Customer";
    private static final String EXISTING_CUSTOMER_DEFAULT_ACCOUNT_NAME = "NGBS Existing Customer";

    /**
     * Create a new Account object for New Business Customer with related Contact
     * and Primary Signatory AccountContactRole and insert them into Salesforce via API.
     *
     * @param ownerUser       Salesforce user that will be the owner of the resulting account
     *                        (usually, a sales user used for testing)
     * @param currencyISOCode ISO code for account's currency
     *                        (e.g. "USD", "EUR", etc...)
     * @return Account object with default parameters and ID from Salesforce
     * @throws Exception in case of malformed query, DB or network errors.
     */
    public static Account createNewCustomerAccountInSFDC(User ownerUser, String currencyISOCode)
            throws Exception {
        var newAccount = new Account();

        var uniqueAccountName = getUniqueAccountName(NEW_CUSTOMER_DEFAULT_ACCOUNT_NAME);
        newAccount.setName(uniqueAccountName);

        newAccount.setOwnerId(ownerUser.getId());
        setCustomerAccountRecordType(newAccount);
        setDefaultBillingAddress(newAccount);
        newAccount.setCurrencyIsoCode(currencyISOCode);

        CONNECTION_UTILS.insertAndGetIds(newAccount);

        var primaryContact = createContactForAccount(newAccount, ownerUser);

        shortDelay();
        createPrimarySignatoryContactRole(newAccount, primaryContact);

        return newAccount;
    }

    /**
     * Create a new Account object for Existing Business Customer with related Contact
     * and Primary Signatory AccountContactRole and insert them into Salesforce via API.
     *
     * @param billingId       numeric id of the existing account from the billing system
     *                        (e.g. "3547712112")
     * @param ownerUser       Salesforce user that will be the owner of the resulting account
     *                        (usually, a sales user used for testing)
     * @param currencyIsoCode ISO code for account's currency
     *                        (e.g. "USD", "EUR", etc...)
     * @return Account object with default parameters and ID from Salesforce
     * @throws Exception in case of malformed query, DB or network errors.
     */
    public static Account createExistingCustomerAccountInSFDC(String billingId, User ownerUser, String currencyIsoCode)
            throws Exception {
        var existingAccount = new Account();

        var uniqueAccountName = getUniqueAccountName(EXISTING_CUSTOMER_DEFAULT_ACCOUNT_NAME);
        existingAccount.setName(uniqueAccountName);

        setRandomEnterpriseAccountId(existingAccount);

        existingAccount.setOwnerId(ownerUser.getId());
        existingAccount.setBilling_ID__c(billingId);
        existingAccount.setRC_Account_Status__c(PAID_RC_ACCOUNT_STATUS);
        setCustomerAccountRecordType(existingAccount);
        setDefaultBillingAddress(existingAccount);
        existingAccount.setCurrencyIsoCode(currencyIsoCode);

        CONNECTION_UTILS.insertAndGetIds(existingAccount);

        var primaryContact = createContactForAccount(existingAccount, ownerUser);

        shortDelay();
        createPrimarySignatoryContactRole(existingAccount, primaryContact);

        return existingAccount;
    }

    /**
     * Create a new Account object for New Business Customer without related Contact
     * and AccountContactRole and insert it into Salesforce via API.
     *
     * @param ownerUser       Salesforce user that will be the owner of the resulting account
     *                        (usually, a sales user used for testing)
     * @param currencyIsoCode ISO code for account's currency
     *                        (e.g. "USD", "EUR", etc...)
     * @return Account object with default parameters and ID from Salesforce
     * @throws Exception in case of malformed query, DB or network errors.
     */
    public static Account createNewCustomerAccountWithoutContactInSFDC(User ownerUser, String currencyIsoCode)
            throws Exception {
        var newAccount = new Account();

        var uniqueAccountName = getUniqueAccountName(NEW_CUSTOMER_DEFAULT_ACCOUNT_NAME);
        newAccount.setName(uniqueAccountName);

        newAccount.setOwnerId(ownerUser.getId());
        setCustomerAccountRecordType(newAccount);
        setDefaultBillingAddress(newAccount);
        newAccount.setCurrencyIsoCode(currencyIsoCode);

        CONNECTION_UTILS.insertAndGetIds(newAccount);

        return newAccount;
    }

    /**
     * Generate unique account's name for Account.Name field.
     *
     * @param defaultAccountName default account's name used as a part of the unique name
     *                           (e.g. "Default Account Name")
     * @return unique string with Account name
     */
    private static String getUniqueAccountName(String defaultAccountName) {
        return UUID.randomUUID().toString().substring(0, 23)
                + " " + defaultAccountName
                + " " + (new SimpleDateFormat("MM/dd HH:mm").format(new Date()));
    }
}
