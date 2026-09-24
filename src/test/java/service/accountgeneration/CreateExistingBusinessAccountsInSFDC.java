package service.accountgeneration;

import model.accountgeneration.CreateExistingBusinessAccountsInSfdcDTO;
import utilities.JsonUtils;
import com.sforce.soap.enterprise.sobject.Account;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static internal.reporting.ServiceTaskLogger.*;
import static utilities.Constants.BASE_URL;
import static utilities.ngbs.NGBSRestApiClient.getAccountInNGBS;
import static utilities.ngbs.NGBSRestApiClient.getPackageSummary;
import static utilities.salesforce.sobjectfactories.AccountContactRoleFactory.createAccountContactRole;
import static utilities.salesforce.sobjectfactories.AccountFactory.createExistingCustomerAccountInSFDC;
import static utilities.salesforce.sobjecthelper.AccountContactRoleHelper.ACCOUNTS_PAYABLE_ROLE;
import static utilities.salesforce.sobjecthelper.AccountHelper.getPrimaryContactOnAccount;
import static utilities.salesforce.sobjectutils.UserUtils.getUserByProfile;
import static io.qameta.allure.Allure.step;

/**
 * Special task for creating Existing Business accounts in SFDC.
 * <br/><br/>
 * This task creates:
 * <p> - corresponding account in NGBS, with tester flags removed, with discounts/contract (if the data provided) </p>
 * <p> - Existing Business Customer Account in SFDC, with default or custom values (if the data provided) </p>
 * <p> - related SFDC objects like Contact, Account Contact Role, etc... </p>
 * <br/>
 * Note: for 'sf.createExistingBusinessAccountsData' list of SFDC EB Accounts Data should be in a JSON format:
 * <pre><code class='json'>
 * [
 *   {
 *     "accountName": "Test_Account_1365",
 *     "serviceType": "Office",
 *     "billingAddress":
 *     {
 *       "country": "United States",
 *       "state": "NY",
 *       "city": "New York",
 *       "street": "Wall Street, 1",
 *       "postalCode": "10007"
 *     },
 *     "contact":
 *     {
 *       "firstName": "Contact_First_Name_1365",
 *       "lastName": "Contact_Last_Name_1365",
 *       "email": "test.mail_1365@ringcentral.com",
 *       "phone": "+37544678904321"
 *     },
 *     "ngbsAccountData":
 *     {
 *       "scenario": "office.paid(18,3)",
 *       "contract":
 *       {
 *         "contractExtId": "Office",
 *         "contractProduct":
 *         {
 *           "name": "DigitalLine Unlimited Standard",
 *           "dataName": "LC_DL-UNL_50",
 *           "cbox": "",
 *           "chargeTerm": "Monthly",
 *           "price": "30.99",
 *           "yourPrice": "30.99",
 *           "quantity": 30,
 *           "existingQuantity": 30,
 *           "discount": 0,
 *           "discountType": "%"
 *         }
 *       },
 *       "discounts":
 *       [
 *         {
 *           "name": "Polycom VVX 501 Color Touchscreen Phone with 1 Expansion Module",
 *           "dataName": "LC_HD_139",
 *           "cbox": "Phones",
 *           "chargeTerm": "One - Time",
 *           "price": "499.00",
 *           "yourPrice": "449.10",
 *           "quantity": 0,
 *           "discount": 10,
 *           "discountType": "%"
 *         }
 *       ]
 *     }
 *   }
 * ]
 * </code></pre>
 *
 * @see CreateExistingBusinessAccountsInSfdcDTO
 */
public class CreateExistingBusinessAccountsInSFDC extends BaseAccountGeneration {

    @Test
    @DisplayName("Create Existing Business account(s) in SFDC")
    public void test() throws IOException {
        var resultsFile = initializeAndGetResultsFile("new_sfdc_eb_accounts");
        var existingBusinessAccountsData = getExistingBusinessAccountsData();

        var processedData = new ArrayList<CreateExistingBusinessAccountsInSfdcDTO>();
        for (var data : existingBusinessAccountsData) {
            step("Create an Existing Business Account in SFDC with Name = '" + data.accountName + "'", () -> {
                if (data.ngbsAccountData == null) {
                    throw new IllegalArgumentException(
                            "'ngbsAccountData' parameter is not provided for the current Account data object! \n" +
                                    "Account data: " + data);
                }

                createAccountInNGBS(data.ngbsAccountData);
                createAccountWithRelatedObjectsInSFDC(data);

                processedData.add(data);
                updateResultsFile(resultsFile, processedData);
            });
        }

        logResults(resultsFile);
    }

    /**
     * Create Account object for Existing Business Customer with default and custom user-defined parameters,
     * related records, like Contact, AccountContactRole, and insert them into Salesforce.
     *
     * @param accountData data object parsed from user's input parameter for creating Existing Business Account in SFDC
     *                    (with custom data values, like Account's Billing Address or Account's Contact First Name...)
     * @throws Exception in case of malformed query, DB or network errors.
     */
    private void createAccountWithRelatedObjectsInSFDC(CreateExistingBusinessAccountsInSfdcDTO accountData) throws Exception {
        var sfdcAccount = createExistingBusinessAccount(accountData);

        var sfdcContact = getPrimaryContactOnAccount(sfdcAccount);
        step("Create non-primary Accounts Payable contact role for the Account and its related Contact", () -> {
            createAccountContactRole(sfdcAccount, sfdcContact, ACCOUNTS_PAYABLE_ROLE, false);
        });

        if (accountData.contact != null) {
            step("Update Account's Contact fields using provided user's input data", () -> {
                sfdcContact.setFirstName(accountData.contact.firstName);
                sfdcContact.setLastName(accountData.contact.lastName);
                sfdcContact.setEmail(accountData.contact.email);
                sfdcContact.setPhone(accountData.contact.phone);

                enterpriseConnectionUtils.update(sfdcContact);
            });
        }

        if (accountData.billingAddress != null) {
            step("Update Account's Billing Address fields using provided user's input data", () -> {
                sfdcAccount.setBillingCountry(accountData.billingAddress.country);
                sfdcAccount.setBillingCity(accountData.billingAddress.city);
                sfdcAccount.setBillingStreet(accountData.billingAddress.street);
                sfdcAccount.setBillingState(accountData.billingAddress.state);
                sfdcAccount.setBillingPostalCode(accountData.billingAddress.postalCode);

                enterpriseConnectionUtils.update(sfdcAccount);
            });
        }

        accountData.accountURL = BASE_URL + "/" + sfdcAccount.getId();
    }

    /**
     * Create Account object for Existing Business Customer with Billing ID and Enterprise ID from NGBS,
     * custom account's data from user's input and insert it into Salesforce via API.
     *
     * @param accountData data object parsed from user's input parameter for creating Existing Business Account in SFDC
     * @throws Exception in case of malformed query, DB or network errors.
     */
    @Step("Create Existing Business Account with related Contact and Primary Signatory AccountContactRole in SFDC")
    private Account createExistingBusinessAccount(CreateExistingBusinessAccountsInSfdcDTO accountData) throws Exception {
        var billingId = accountData.ngbsAccountData.billingId;

        //  get account's package id and version in NGBS to retrieve Currency and Brand
        var accountDTO = getAccountInNGBS(billingId);
        var catalogID = accountDTO.packages[0].catalogId;
        var versionID = accountDTO.packages[0].version;
        var packageInfo = getPackageSummary(catalogID, versionID);

        var salesUser = getUserByProfile("Sales Rep - Lightning");
        var existingBusinessAccount = createExistingCustomerAccountInSFDC(billingId, salesUser, packageInfo.currency);

        existingBusinessAccount.setName(accountData.accountName);
        existingBusinessAccount.setRC_User_ID__c(accountData.ngbsAccountData.rcUserId);
        existingBusinessAccount.setRC_Brand__c(packageInfo.getLabelsBrandName());
        existingBusinessAccount.setService_Type__c(accountData.serviceType);
        existingBusinessAccount.setRC_Service_name__c(accountData.serviceType);
        enterpriseConnectionUtils.update(existingBusinessAccount);

        return existingBusinessAccount;
    }

    /**
     * Get a collection of input data objects for creating Existing Business Accounts in SFDC
     * from the system property variable.
     *
     * @return list of input data objects to create Existing Business Account(s) with
     */
    private List<CreateExistingBusinessAccountsInSfdcDTO> getExistingBusinessAccountsData() {
        var existingBusinessAccountsDataInputString = System.getProperty("sf.createExistingBusinessAccountsData");
        if (existingBusinessAccountsDataInputString == null || existingBusinessAccountsDataInputString.isBlank()) {
            throw new IllegalArgumentException("No Account Input Data have been provided! " +
                    "Make sure to add SFDC account and contact data, as well as data for creating account in NGBS " +
                    "for this task via 'sf.createExistingBusinessAccountsData' parameter!");
        }

        var accountsDataParsed = JsonUtils.readJson(
                existingBusinessAccountsDataInputString, CreateExistingBusinessAccountsInSfdcDTO[].class);
        return List.of(accountsDataParsed);
    }
}
